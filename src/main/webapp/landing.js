(() => {
  'use strict';
  // Barra Apollo/Login fixa: ganha sombra mais forte quando a página já rolou.
  const navbar = document.querySelector('.navbar');
  if (navbar) {
    const onScroll = () => navbar.classList.toggle('is-scrolled', window.scrollY > 8);
    onScroll();
    window.addEventListener('scroll', onScroll, { passive: true });
  }

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

  // Equipe: alterna entre os integrantes e a foto do grupo (Ver equipe / Ver integrantes).
  const equipeSection = document.getElementById('equipe');
  const membersView = document.getElementById('team-members-view');
  const groupView = document.getElementById('team-group-view');
  const showGroupBtn = document.getElementById('team-show-group');
  const showMembersBtn = document.getElementById('team-show-members');
  if (equipeSection && membersView && groupView && showGroupBtn && showMembersBtn) {
    const showTeamView = (group) => {
      membersView.hidden = group;
      groupView.hidden = !group;
      equipeSection.classList.toggle('is-group', group);
      showGroupBtn.setAttribute('aria-expanded', String(group));
      showMembersBtn.setAttribute('aria-expanded', String(group));
      (group ? showMembersBtn : showGroupBtn).focus({ preventScroll: true });
    };
    showGroupBtn.addEventListener('click', () => showTeamView(true));
    showMembersBtn.addEventListener('click', () => showTeamView(false));
  }

})();