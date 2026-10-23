(() => {
  'use strict';
  const data = {
    inicio: {
      title: 'Índice de Sustentabilidade', color: '#a8be00', rule: '#202020',
      html: 'O Índice de Sustentabilidade (IS) é uma avaliação inteligente que analisa cada lote de painéis solares com base em três critérios essenciais: <em class="blue">eficiência</em>, <em class="red">vida útil</em> e <em class="green">retorno financeiro</em>. Com essas informações, o sistema gera uma nota que auxilia na tomada de <em class="blue">decisões mais seguras e estratégicas</em>.'
    },
    fin: {
      title: 'Retorno financeiro', color: '#09a84e', rule: '#09a84e',
      html: 'Avalia o custo do painel e seu potencial de geração de energia para indicar se o investimento tem retorno financeiro vantajoso.'
    },
    efi: {
      title: 'Eficiência energética', color: '#3caeff', rule: '#3caeff',
      html: 'Analisa a capacidade dos painéis de transformar a luz solar em energia e ajuda a identificar perdas de rendimento e oportunidades de melhoria.'
    },
    vida: {
      title: 'Vida útil estimada', color: '#ff6767', rule: '#ff6767',
      html: 'Considera o estado dos painéis e o tempo de uso para estimar a vida útil restante e apoiar decisões de manutenção ou substituição.'
    }
  };
  const tabs = Array.from(document.querySelectorAll('.is-option'));
  const content = document.getElementById('is-content');
  const showCriterion = (key) => {
    const item = data[key];
    if (!item || !content) return;
    tabs.forEach((tab) => {
      const active = tab.dataset.criterion === key;
      tab.classList.toggle('on-fin', active && key === 'fin');
      tab.classList.toggle('on-efi', active && key === 'efi');
      tab.classList.toggle('on-vida', active && key === 'vida');
      tab.setAttribute('aria-selected', String(active));
    });
    content.innerHTML = `<h3 style="color:${item.color}"><span>${item.title}</span><i class="is-rule" style="background:${item.rule}" aria-hidden="true"></i></h3><p>${item.html}</p>`;
  };
  tabs.forEach((tab, index) => {
    tab.addEventListener('click', () => showCriterion(tab.dataset.criterion));
    tab.addEventListener('keydown', (event) => {
      if (!['ArrowDown', 'ArrowUp', 'Home', 'End'].includes(event.key)) return;
      event.preventDefault();
      let next = index;
      if (event.key === 'ArrowDown') next = (index + 1) % tabs.length;
      if (event.key === 'ArrowUp') next = (index - 1 + tabs.length) % tabs.length;
      if (event.key === 'Home') next = 0;
      if (event.key === 'End') next = tabs.length - 1;
      tabs[next].focus();
      showCriterion(tabs[next].dataset.criterion);
    });
  });
  // A capa usa apenas navegação por âncora enquanto não houver uma rota real de login.
  showCriterion('inicio');

  // Barra Apollo/Login fixa: ganha sombra mais forte quando a página já rolou.
  const navbar = document.querySelector('.navbar');
  if (navbar) {
    const onScroll = () => navbar.classList.toggle('is-scrolled', window.scrollY > 8);
    onScroll();
    window.addEventListener('scroll', onScroll, { passive: true });
  }

  // Clicar no IS (o círculo verde) volta para o texto inicial e desmarca os critérios.
  document.getElementById('is-emblem')?.addEventListener('click', () => showCriterion('inicio'));

  // Funcionalidades: abas Técnico / Operador / Gerente com pílula deslizante e troca de texto + tela do app.
  const funcTabs = document.getElementById('func-tabs');
  if (funcTabs) {
    const fTabs = Array.from(funcTabs.querySelectorAll('.func-tab'));
    const fPanels = Array.from(document.querySelectorAll('.func-panel'));
    const shot = document.getElementById('func-shot');
    const movePill = () => {
      const active = funcTabs.querySelector('.func-tab.is-active');
      if (!active) return;
      funcTabs.style.setProperty('--pill-x', `${active.offsetLeft}px`);
      funcTabs.style.setProperty('--pill-w', `${active.offsetWidth}px`);
    };
    const swapShot = (role) => {
      if (!shot) return;
      const next = shot.dataset[`src${role.charAt(0).toUpperCase()}${role.slice(1)}`];
      if (!next || shot.getAttribute('src') === next) return;
      shot.classList.add('is-swapping');
      window.setTimeout(() => {
        shot.onload = () => shot.classList.remove('is-swapping');
        shot.src = next;
        if (shot.complete) shot.classList.remove('is-swapping');
      }, 220);
    };
    const selectRole = (tab, focus) => {
      const role = tab.dataset.role;
      fTabs.forEach((t) => {
        const on = t === tab;
        t.classList.toggle('is-active', on);
        t.setAttribute('aria-selected', String(on));
        t.tabIndex = on ? 0 : -1;
      });
      fPanels.forEach((panel) => {
        const on = panel.id === `panel-${role}`;
        panel.hidden = !on;
        panel.classList.toggle('is-active', on);
      });
      movePill();
      swapShot(role);
      if (focus) tab.focus();
    };
    fTabs.forEach((tab, index) => {
      tab.addEventListener('click', () => selectRole(tab, false));
      tab.addEventListener('keydown', (event) => {
        if (!['ArrowRight', 'ArrowLeft', 'Home', 'End'].includes(event.key)) return;
        event.preventDefault();
        let next = index;
        if (event.key === 'ArrowRight') next = (index + 1) % fTabs.length;
        if (event.key === 'ArrowLeft') next = (index - 1 + fTabs.length) % fTabs.length;
        if (event.key === 'Home') next = 0;
        if (event.key === 'End') next = fTabs.length - 1;
        selectRole(fTabs[next], true);
      });
    });
    movePill();
    window.addEventListener('resize', movePill);
    document.fonts?.ready.then(movePill);
  }

  // Carrossel contínuo da equipe: pausa ao passar o mouse e aceita arrasto manual.
  const teamWindow = document.getElementById('team-window');
  const teamTrack = document.getElementById('team-track');
  const reducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  if (teamWindow && teamTrack && !reducedMotion) {
    const originalCount = 11;
    let paused = false;
    let dragging = false;
    let lastTime = 0;
    let startX = 0;
    let startScroll = 0;
    let animationFrame = 0;
    const speed = 48; // velocidade suave, semelhante ao movimento apresentado no vídeo
    const loopWidth = () => {
      const originals = Array.from(teamTrack.children).slice(0, originalCount);
      if (!originals.length) return 0;
      const style = getComputedStyle(teamTrack);
      const gap = parseFloat(style.columnGap || style.gap || '0') || 0;
      return originals.reduce((total, card) => total + card.getBoundingClientRect().width, 0) + gap * originalCount;
    };
    const animate = (time) => {
      if (lastTime && !paused && !dragging) {
        const delta = Math.min((time - lastTime) / 1000, 0.06);
        const width = loopWidth();
        teamWindow.scrollLeft += speed * delta;
        if (width > 0 && teamWindow.scrollLeft >= width) teamWindow.scrollLeft -= width;
      }
      lastTime = time;
      animationFrame = window.requestAnimationFrame(animate);
    };
    teamWindow.addEventListener('mouseenter', () => { paused = true; });
    teamWindow.addEventListener('mouseleave', () => { paused = false; });
    teamWindow.addEventListener('pointerdown', (event) => {
      if (event.pointerType === 'mouse' && event.button !== 0) return;
      dragging = true;
      startX = event.clientX;
      startScroll = teamWindow.scrollLeft;
      teamWindow.classList.add('is-dragging');
      if (event.pointerType === 'mouse') teamWindow.setPointerCapture?.(event.pointerId);
    });
    teamWindow.addEventListener('pointermove', (event) => {
      if (dragging) teamWindow.scrollLeft = startScroll - (event.clientX - startX);
    });
    const endDrag = () => { dragging = false; teamWindow.classList.remove('is-dragging'); };
    teamWindow.addEventListener('pointerup', endDrag);
    teamWindow.addEventListener('pointercancel', endDrag);
    window.addEventListener('pagehide', () => window.cancelAnimationFrame(animationFrame), { once: true });
    animationFrame = window.requestAnimationFrame(animate);
  }

})();