// /assets/js/app.js
document.addEventListener('DOMContentLoaded', function () {
  // ----- Carousel (yours, unchanged) -----
  const carouselEl = document.getElementById('heroCarousel');
  if (carouselEl && window.bootstrap?.Carousel) {
    const carousel = new bootstrap.Carousel(carouselEl, {
      interval: 10000,
      pause: false,
      wrap: true,
      touch: true,
      keyboard: true
    });
    carouselEl.addEventListener('slid.bs.carousel', () => carousel.cycle());
    carouselEl.querySelectorAll('[data-bs-target="#heroCarousel"]').forEach(btn => {
      btn.addEventListener('click', () => setTimeout(() => carousel.cycle(), 0));
    });
  }

  // ----- Scroll reveal (sections + anything with .anim) -----
  const targets = document.querySelectorAll('section, .anim');

  const show = el => el.classList.add('in');
  const hide = el => el.classList.remove('in');

  if ('IntersectionObserver' in window) {
    const io = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        const el = entry.target;
        const repeat = el.hasAttribute('data-repeat'); // if present, animate every time it enters

        if (entry.isIntersecting) {
          show(el);
          if (!repeat) io.unobserve(el); // performance: stop observing if one-off
        } else if (repeat) {
          hide(el); // fade-out / slide-out on exit if repeat enabled
        }
      });
    }, {
      root: null,
      // Trigger a bit before it fully enters (tweak as you like)
      rootMargin: '0px 0px -10% 0px',
      threshold: 0.12
    });

    targets.forEach(t => io.observe(t));
  } else {
    // Fallback
    targets.forEach(show);
  }
});

// Map data-duration / data-delay to CSS custom properties
document.querySelectorAll('.anim').forEach(el => {
  const d = el.getAttribute('data-duration');
  if (d) el.style.setProperty('--duration', d);

  const delay = el.getAttribute('data-delay');
  if (delay) el.style.setProperty('--delay', delay);
});

// ==== Count-up for #about-us stats ====
(function () {
  const els = document.querySelectorAll('#about-us [data-count-to]');
  if (!els.length || !('IntersectionObserver' in window)) return;

  const lerp = (a, b, t) => a + (b - a) * t;
  const format = (n) => {
    // Keep one decimal for values under 10 that include a decimal (like 4.9)
    return (n < 10 && String(targetFrom(el)).includes('.'))
      ? n.toFixed(1)
      : Math.round(n);
  };

  const targetFrom = (el) => parseFloat(el.getAttribute('data-count-to')) || 0;

  const animate = (el) => {
    const target = targetFrom(el);
    const dur = 900; // ms
    const start = performance.now();
    const startVal = 0;

    const step = (now) => {
      const t = Math.min(1, (now - start) / dur);
      const eased = 1 - Math.pow(1 - t, 3); // easeOutCubic
      const val = lerp(startVal, target, eased);

      // If target has one decimal, keep one decimal during animation
      if (String(target).includes('.') && target < 10) {
        el.textContent = val.toFixed(1);
      } else {
        el.textContent = Math.round(val);
      }

      if (t < 1) requestAnimationFrame(step);
    };
    requestAnimationFrame(step);
  };

  const seen = new WeakSet();
  const io = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting && !seen.has(entry.target)) {
        seen.add(entry.target);
        animate(entry.target);
      }
    });
  }, { threshold: 0.25 });

  els.forEach((el) => io.observe(el));
})();

// ===== Simple gallery modal for Our Selections =====
(function () {
  const modalEl = document.getElementById('galleryModal');
  if (!modalEl) return;

  const imgEl = modalEl.querySelector('#galleryImage');
  document.addEventListener('click', (e) => {
    const btn = e.target.closest('[data-gallery]');
    if (!btn) return;
    const src = btn.getAttribute('data-gallery');
    if (src) {
      imgEl.src = src;
      imgEl.alt = btn.closest('.selection-card')?.querySelector('img')?.alt || 'Selected item';
    }
  });

  modalEl.addEventListener('hidden.bs.modal', () => { imgEl.src = ''; });
})();

// ===== Dropdown hover with delay =====
document.querySelectorAll('.navbar .dropdown').forEach(dropdown => {
  let timeout;

  dropdown.addEventListener('mouseenter', () => {
    clearTimeout(timeout);
    dropdown.classList.add('show');
    dropdown.querySelector('.dropdown-menu').classList.add('show');
  });

  dropdown.addEventListener('mouseleave', () => {
    timeout = setTimeout(() => {
      dropdown.classList.remove('show');
      dropdown.querySelector('.dropdown-menu').classList.remove('show');
    }, 200); // delay before hiding
  });
});

window.addEventListener('DOMContentLoaded', event => {

    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        // Uncomment Below to persist sidebar toggle between refreshes
        // if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
        //     document.body.classList.toggle('sb-sidenav-toggled');
        // }
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }

});


// Data sample demo

window.addEventListener('DOMContentLoaded', event => {
    // Simple-DataTables
    // https://github.com/fiduswriter/Simple-DataTables/wiki

    const datatablesSimple = document.getElementById('datatablesSimple');
    if (datatablesSimple) {
        new simpleDatatables.DataTable(datatablesSimple);
    }
});
