(() => {
  // Helper: run after DOM is ready (or immediately if already ready)
  const onReady = (fn) => {
    if (document.readyState === 'loading') {
      document.addEventListener('DOMContentLoaded', fn, { once: true });
    } else {
      fn();
    }
  };

  onReady(() => {
    // ===== Carousel (yours, unchanged) =====
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

    // ===== Scroll reveal (sections + anything with .anim) =====
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
        rootMargin: '0px 0px -10% 0px',
        threshold: 0.12
      });

      targets.forEach(t => io.observe(t));
    } else {
      // Fallback
      targets.forEach(show);
    }

    // ===== Map data-duration / data-delay to CSS custom properties =====
    document.querySelectorAll('.anim').forEach(el => {
      const d = el.getAttribute('data-duration');
      if (d) el.style.setProperty('--duration', d);
      const delay = el.getAttribute('data-delay');
      if (delay) el.style.setProperty('--delay', delay);
    });

    // ===== Count-up for #about-us stats =====
    (function () {
      const els = document.querySelectorAll('#about-us [data-count-to]');
      if (!els.length || !('IntersectionObserver' in window)) return;

      const lerp = (a, b, t) => a + (b - a) * t;
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

          // If target has one decimal and < 10, keep one decimal during animation
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
        }, 200);
      });
    });

    // ===== Toggle the side navigation =====
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
      // Uncomment Below to persist sidebar toggle between refreshes
      // if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
      //   document.body.classList.toggle('sb-sidenav-toggled');
      // }
      sidebarToggle.addEventListener('click', event => {
        event.preventDefault();
        document.body.classList.toggle('sb-sidenav-toggled');
        localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
      });
    }

    // ===== Data sample demo (Simple-DataTables) =====
    // https://github.com/fiduswriter/Simple-DataTables/wiki
    const datatablesSimple = document.getElementById('datatablesSimple');
    if (datatablesSimple && window.simpleDatatables?.DataTable) {
      new simpleDatatables.DataTable(datatablesSimple);
    }

    // ===== Product page: thumbnail gallery swap =====
    (function () {
      const gallery = document.getElementById('gallery');
      if (!gallery) return;

      const mainImage = gallery.querySelector('#mainImage');
      const thumbs = Array.from(gallery.querySelectorAll('.thumb'));

      function activate(btn) {
        thumbs.forEach(t => t.classList.remove('thumb-active'));
        btn.classList.add('thumb-active');
        const full = btn.getAttribute('data-full');
        if (full && mainImage) {
          mainImage.src = full;
          // Keep alt text in sync with the chosen thumb’s img
          const thumbImg = btn.querySelector('img');
          if (thumbImg && thumbImg.alt) mainImage.alt = thumbImg.alt;
        }
      }

      thumbs.forEach(btn => {
        btn.addEventListener('click', () => activate(btn));
        // Keyboard accessibility (Enter/Space)
        btn.addEventListener('keydown', (e) => {
          if (e.key === 'Enter' || e.key === ' ') {
            e.preventDefault();
            activate(btn);
          }
        });
      });
    })();
	
	(() => {
	document.addEventListener('click', (e) => {
	    const btn = e.target.closest('.btn-qty');
	    if (!btn) return;
	    const display = document.getElementById('qtyDisplay');
		const input = document.getElementById('qtyInput');
	    if (!display) return;
	    let current = parseInt(display.textContent, 10) || 1;
	    if (btn.dataset.action === 'plus') {
	      current++;
	    } else if (btn.dataset.action === 'minus') {
	      current = Math.max(1, current - 1); // prevent going below 1
	    }
	    display.textContent = current;
		input.value = current;
	  });
	})();
  });
})();