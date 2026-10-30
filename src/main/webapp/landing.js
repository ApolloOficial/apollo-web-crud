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

  // Carrossel dos diferenciais: IS <-> ApolloIA (setas, bolinhas, swipe e rotação automática).
  const diffCarousel = document.getElementById('diff-carousel');
  if (diffCarousel) {
    const dTrack = document.getElementById('diff-track');
    const dSlides = Array.from(dTrack.querySelectorAll('.diff-slide'));
    const dDots = Array.from(diffCarousel.querySelectorAll('.diff-dot'));
    const dPrev = document.getElementById('diff-prev');
    const dNext = document.getElementById('diff-next');
    const dViewport = diffCarousel.querySelector('.diff-viewport');
    const autoDelay = 12000;
    let dIndex = 0;
    let autoTimer = 0;
    let autoStopped = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    let hovering = false;
    let visible = true;
    const goTo = (index) => {
      dIndex = (index + dSlides.length) % dSlides.length;
      dTrack.style.transform = `translateX(${-100 * dIndex}%)`;
      dSlides.forEach((slide, i) => {
        const on = i === dIndex;
        slide.classList.toggle('is-current', on);
        slide.setAttribute('aria-hidden', String(!on));
        slide.toggleAttribute('inert', !on);
      });
      dDots.forEach((dot, i) => {
        const on = i === dIndex;
        dot.classList.toggle('is-on', on);
        if (on) dot.setAttribute('aria-current', 'true'); else dot.removeAttribute('aria-current');
      });
    };
    const clearAuto = () => { window.clearTimeout(autoTimer); autoTimer = 0; };
    const scheduleAuto = () => {
      clearAuto();
      if (autoStopped || hovering || !visible || document.hidden) return;
      autoTimer = window.setTimeout(() => { goTo(dIndex + 1); scheduleAuto(); }, autoDelay);
    };
    // Depois que a pessoa usa os controles, a rotação automática para de vez.
    const userGo = (index) => { autoStopped = true; clearAuto(); goTo(index); };
    dPrev?.addEventListener('click', () => userGo(dIndex - 1));
    dNext?.addEventListener('click', () => userGo(dIndex + 1));
    dDots.forEach((dot) => dot.addEventListener('click', () => userGo(Number(dot.dataset.slide))));
    diffCarousel.addEventListener('keydown', (event) => {
      if (event.target.closest('.is-options')) return; // setas ↑/↓ já navegam entre os critérios do IS
      if (event.key === 'ArrowLeft') { event.preventDefault(); userGo(dIndex - 1); }
      if (event.key === 'ArrowRight') { event.preventDefault(); userGo(dIndex + 1); }
    });
    // Swipe em telas de toque.
    let swipeX = null;
    dViewport.addEventListener('pointerdown', (event) => {
      if (event.pointerType === 'mouse') return;
      swipeX = event.clientX;
    });
    dViewport.addEventListener('pointerup', (event) => {
      if (swipeX === null) return;
      const dx = event.clientX - swipeX;
      swipeX = null;
      if (Math.abs(dx) > 50) userGo(dIndex + (dx < 0 ? 1 : -1));
    });
    dViewport.addEventListener('pointercancel', () => { swipeX = null; });
    // Pausa enquanto o mouse está sobre o carrossel, a aba está oculta ou ele está fora da tela.
    diffCarousel.addEventListener('mouseenter', () => { hovering = true; clearAuto(); });
    diffCarousel.addEventListener('mouseleave', () => { hovering = false; scheduleAuto(); });
    diffCarousel.addEventListener('focusin', () => { hovering = true; clearAuto(); });
    diffCarousel.addEventListener('focusout', () => { hovering = false; scheduleAuto(); });
    document.addEventListener('visibilitychange', scheduleAuto);
    if ('IntersectionObserver' in window) {
      new IntersectionObserver(([entry]) => { visible = entry.isIntersecting; scheduleAuto(); }, { threshold: 0.35 }).observe(diffCarousel);
    }
    goTo(0);
    scheduleAuto();
  }

  // ApolloIA: clicar em Analisar / Orientar / Ajudar troca o texto; clicar no robô volta ao texto inicial.
  const iaContent = document.getElementById('ia-content');
  if (iaContent) {
    const iaHello = { from: 'bot', text: 'Olá! Como posso te ajudar na gestão do seu sistema solar?' };
    const iaTexts = {
      analisar: { title: 'Analisar', chat: [iaHello,
        { from: 'user', text: 'A eficiência da filial está caindo, analise possíveis falhas' },
        { from: 'bot', text: 'Analisando... Há vários inversores desativados, o que pode ter afetado.' }] },
      orientar: { title: 'Orientar', chat: [iaHello,
        { from: 'user', text: 'Porque é preciso fazer uma manutenção preventiva?' },
        { from: 'bot', text: 'Porque ela previne possíveis danos do seu painel.' }] },
      ajudar: { title: 'Ajudar', chat: [iaHello,
        { from: 'user', text: 'Como posso melhorar a eficiência dos meus painéis?' },
        { from: 'bot', text: 'Posso ajudar! Vamos analisar o histórico e identificar possíveis melhorias.' }] }
    };
    const iaInitial = iaContent.innerHTML;
    const iaTags = Array.from(document.querySelectorAll('.ia-tag'));
    const showIa = (key) => {
      const item = iaTexts[key];
      iaTags.forEach((tag) => tag.setAttribute('aria-pressed', String(!!item && tag.dataset.ia === key)));
      iaContent.innerHTML = item
        ? `<h3><span>${item.title}</span><i class="is-rule" aria-hidden="true"></i></h3><div class="ia-chat">${item.chat.map((m, i) => `<div class="ia-msg ia-msg-${m.from}" style="animation-delay:${i * 0.5}s">${m.text}</div>`).join('')}</div>`
        : iaInitial;
    };
    iaTags.forEach((tag) => tag.addEventListener('click', () => showIa(tag.dataset.ia)));
    document.getElementById('ia-emblem')?.addEventListener('click', () => showIa(null));
  }

})();