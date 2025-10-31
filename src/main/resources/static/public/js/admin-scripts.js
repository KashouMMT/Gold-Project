/*!
 * Start Bootstrap - SB Admin v7.0.7 (https://startbootstrap.com/template/sb-admin)
 * Copyright 2013-2023 Start Bootstrap
 * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
 */
// 
// Scripts
// 

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
    // =========================
    // 1) Toggle the side navigation (SB Admin)
    // =========================
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
      // Uncomment Below to persist sidebar toggle between refreshes
      // if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
      //   document.body.classList.toggle('sb-sidenav-toggled');
      // }
      sidebarToggle.addEventListener('click', (event) => {
        event.preventDefault();
        document.body.classList.toggle('sb-sidenav-toggled');
        localStorage.setItem(
          'sb|sidebar-toggle',
          document.body.classList.contains('sb-sidenav-toggled')
        );
      });
    }

    // =========================
    // 2) Toggle-input-wrapper (dropdown <-> input)
    // =========================
    document.querySelectorAll('.toggle-input-wrapper').forEach((wrapper) => {
      const dropdown  = wrapper.querySelector('[data-type="dropdown"]');
      const input     = wrapper.querySelector('[data-type="input"]');
      const addBtn    = wrapper.querySelector('[data-type="add"]');
      const removeBtn = wrapper.querySelector('[data-type="remove"]');

      // start state
      if (dropdown) dropdown.disabled = false;
      if (input)    input.disabled = true;

      addBtn?.addEventListener('click', () => {
        dropdown?.classList.add('d-none'); if (dropdown) dropdown.disabled = true;
        addBtn.classList.add('d-none');
        input?.classList.remove('d-none'); if (input) { input.disabled = false; input.focus(); }
        removeBtn?.classList.remove('d-none');
      });

      removeBtn?.addEventListener('click', () => {
        if (input) {
          input.classList.add('d-none'); input.disabled = true; input.value = '';
        }
        removeBtn.classList.add('d-none');
        dropdown?.classList.remove('d-none'); if (dropdown) dropdown.disabled = false;
        addBtn.classList.remove('d-none');
      });
    });

    // =========================
    // 3) Dynamic Width/Length blocks (tpl-width / tpl-length)
    // =========================
    {
      const widthsRoot  = document.getElementById('widths-root');
      const tplWidth    = document.getElementById('tpl-width');
      const tplLength   = document.getElementById('tpl-length');
      const btnAddWidth = document.getElementById('js-add-width');

      if (widthsRoot && tplWidth && tplLength && btnAddWidth) {
        function addLengthRow(block, wIdx) {
          const tbody = block.querySelector('tbody.lengths');
          const lIdx = tbody.querySelectorAll('tr').length;
          const html = tplLength.innerHTML
            .replaceAll('__W__', wIdx)
            .replaceAll('__L__', lIdx);
          const frag = document.createRange().createContextualFragment(html);
          tbody.appendChild(frag);
        }

        function addWidthBlock() {
          const wIdx = widthsRoot.querySelectorAll('.width-block').length;
          const frag = document.createRange().createContextualFragment(
            tplWidth.innerHTML.replaceAll('__W__', wIdx)
          );
          widthsRoot.appendChild(frag);
          const block = widthsRoot.querySelectorAll('.width-block')[wIdx];
          addLengthRow(block, wIdx); // seed 1 length for brand-new width
        }

        function reindexAll() {
          const blocks = [...widthsRoot.querySelectorAll('.width-block')];
          blocks.forEach((block, wIdx) => {
            const widthInput = block.querySelector('input[name$=".width"]');
            if (widthInput) widthInput.name = `productWidthDto[${wIdx}].width`;
            const widthId = block.querySelector('input[type="hidden"][name$=".id"]');
            if (widthId) widthId.name = `productWidthDto[${wIdx}].id`;

            const rows = [...block.querySelectorAll('tbody.lengths tr')];
            rows.forEach((tr, lIdx) => {
              tr.querySelectorAll('input[name]').forEach((inp) => {
                if (inp.name.endsWith('.length'))
                  inp.name = `productWidthDto[${wIdx}].productLengthDto[${lIdx}].length`;
                else if (inp.name.endsWith('.weight'))
                  inp.name = `productWidthDto[${wIdx}].productLengthDto[${lIdx}].weight`;
                else if (inp.name.endsWith('.price'))
                  inp.name = `productWidthDto[${wIdx}].productLengthDto[${lIdx}].price`;
                else if (inp.name.endsWith('.id'))
                  inp.name = `productWidthDto[${wIdx}].productLengthDto[${lIdx}].id`;
              });
            });

            // intentionally allow zero-length rows (unchanged from original)
          });
        }

        btnAddWidth.addEventListener('click', () => {
          addWidthBlock();
          reindexAll();
        });

        widthsRoot.addEventListener('click', (e) => {
          if (e.target.closest('.js-add-length')) {
            const block = e.target.closest('.width-block');
            const wIdx = [...widthsRoot.querySelectorAll('.width-block')].indexOf(block);
            addLengthRow(block, wIdx);
            reindexAll();
          }
          if (e.target.closest('.js-remove-length')) {
            e.target.closest('tr').remove();
            reindexAll();
          }
          if (e.target.closest('.js-remove-width')) {
            e.target.closest('.width-block').remove();
            reindexAll();
          }
        });

        reindexAll();
      }
    }

    // =========================
    // 4) Variants table builder + validation (from IIFE)
    // =========================
    {
      const form     = document.getElementById('variantsForm');
      const widthEl  = document.getElementById('widthCount');
      const lengthEl = document.getElementById('lengthCount');
      const tbody    = document.querySelector('#variantsTable tbody');
      const buildBtn = document.getElementById('build');

      if (form && widthEl && lengthEl && tbody && buildBtn) {
        // Regex rules equivalent to @Digits(integer=5, fraction=2) and @Digits(integer=8, fraction=2)
        const RULES = {
          '5_2': /^\d{1,5}(\.\d{1,2})?$/,
          '8_2': /^\d{1,8}(\.\d{1,2})?$/
        };

        const snapshotValues = () => {
          const map = new Map();
          tbody.querySelectorAll('input[name]').forEach((inp) => map.set(inp.name, inp.value));
          return map;
        };

        function createInput(name, { rule = '5_2', step = '0.01', min = '0', placeholder = name } = {}) {
          const input = document.createElement('input');
          input.type = 'text';
          input.name = name;
          input.className = 'form-control';
          input.placeholder = placeholder;
          input.required = true;
          input.dataset.rule = rule;
          input.dataset.step = step;
          input.dataset.min = min;
          input.title =
            rule === '8_2'
              ? 'Up to 8 digits and 2 decimals (e.g., 12345678.90)'
              : 'Up to 5 digits and 2 decimals (e.g., 12345.67)';

          const wrap = document.createElement('div');
          wrap.className = 'd-flex flex-column gap-1';
          const feedback = document.createElement('div');
          feedback.className = 'invalid-feedback';
          feedback.textContent = input.title;
          wrap.appendChild(input);
          wrap.appendChild(feedback);
          return { input, wrapper: wrap };
        }

        function validateField(input) {
          const ruleKey = input.dataset.rule || '5_2';
          const re = RULES[ruleKey];
          const val = (input.value || '').trim();
          const ok = val.length > 0 && re.test(val);
          input.classList.toggle('is-invalid', !ok);
          input.classList.toggle('is-valid', ok);
          return ok;
        }

        function validateAll() {
          let allOK = true;
          tbody.querySelectorAll('input[name]').forEach((inp) => {
            if (!validateField(inp)) allOK = false;
          });
          return allOK;
        }

        function renderTable() {
          const W = Math.max(0, parseInt(widthEl.value) || 0);
          const L = Math.max(0, parseInt(lengthEl.value) || 0);
          const prev = snapshotValues();
          tbody.innerHTML = '';

          if (W === 0 || L === 0) return;

          for (let i = 0; i < W; i++) {
            for (let j = 0; j < L; j++) {
              const tr = document.createElement('tr');

              if (j === 0) {
                const tdWidth = document.createElement('td');
                tdWidth.rowSpan = L;
                const widthName = `width[${i}]`;
                const { input: widthInput, wrapper: widthWrap } = createInput(widthName, { rule: '5_2' });
                if (prev.has(widthName)) widthInput.value = prev.get(widthName);
                tdWidth.appendChild(widthWrap);
                tr.appendChild(tdWidth);
              }

              const tdLength = document.createElement('td');
              const lengthName = `length[${i}][${j}]`;
              const { input: lengthInput, wrapper: lengthWrap } = createInput(lengthName, { rule: '5_2' });
              if (prev.has(lengthName)) lengthInput.value = prev.get(lengthName);
              tdLength.appendChild(lengthWrap);
              tr.appendChild(tdLength);

              const tdPrice = document.createElement('td');
              const priceName = `price[${i}][${j}]`;
              const { input: priceInput, wrapper: priceWrap } = createInput(priceName, { rule: '8_2' });
              if (prev.has(priceName)) priceInput.value = prev.get(priceName);
              tdPrice.appendChild(priceWrap);
              tr.appendChild(tdPrice);

              tbody.appendChild(tr);
            }
          }

          validateAll();
        }

        buildBtn.addEventListener('click', renderTable);
        widthEl.addEventListener('change', renderTable);
        lengthEl.addEventListener('change', renderTable);

        tbody.addEventListener('input', (e) => {
          const target = e.target;
          if (target && target.name) validateField(target);
        });

        form.addEventListener('submit', (e) => {
          if (!validateAll()) {
            e.preventDefault();
            e.stopPropagation();
          }
        });

        renderTable();
      }
    }

    // =========================
    // 5) Submit logging: keys/values going to server
    // =========================
    {
      const form = document.getElementById('variantsForm');
      if (form) {
        form.addEventListener('submit', (e) => {
          const fd = new FormData(e.target);
          console.log('--- SUBMIT KEYS ---');
          for (const [k, v] of fd.entries()) console.log(k, '=', v);
        });
      }
    }

    // =========================
    // 6) Ensure table exists before submit
    // =========================
    {
      const form = document.getElementById('variantsForm');
      if (document.getElementById('addActionFlag').value === 'true' && form) {
        form.addEventListener('submit', (e) => {
          const hasWidth = !!document.querySelector('#variantsTable tbody input[name^="width["]');
          const hasLen   = !!document.querySelector('#variantsTable tbody input[name^="length["]');
          const hasPrice = !!document.querySelector('#variantsTable tbody input[name^="price["]');
          if (!hasWidth || !hasLen || !hasPrice) {
            e.preventDefault();
            alert('Please generate the table first.');
          }
        });
      }
    }

    // =========================
    // 7) Detailed FormData snapshot / grouping logs (from IIFE)
    // =========================
    {
      const form = document.getElementById('variantsForm');
      if (form) {
        form.addEventListener('submit', (e) => {
          const fd = new FormData(form);

          console.log('===== FORM SUBMIT: raw entries =====');
          for (const [k, v] of fd.entries()) console.log(k, '=', v);

          const groups = { width: [], length: [], price: [], other: [] };
          for (const [k, v] of fd.entries()) {
            if (k.startsWith('width[')) groups.width.push([k, v]);
            else if (k.startsWith('length[')) groups.length.push([k, v]);
            else if (k.startsWith('price[')) groups.price.push([k, v]);
            else groups.other.push([k, v]);
          }

          console.log('===== COUNTS =====');
          console.table({
            width: groups.width.length,
            length: groups.length.length,
            price: groups.price.length,
            other: groups.other.length
          });

          console.log('===== width[] (first 10) =====');
          console.table(groups.width.slice(0, 10));
          console.log('===== length[][] (first 10) =====');
          console.table(groups.length.slice(0, 10));
          console.log('===== price[][] (first 10) =====');
          console.table(groups.price.slice(0, 10));

          const qp = new URLSearchParams(fd);
          console.log('===== URL-encoded payload =====');
          console.log(qp.toString());
          // NOTE: no blocking here (unchanged)
        });
      }
    }

    // =========================
    // 8) Image grid manager (existing/new images, max 10)
    // =========================
    {
      const maxImages    = 10;
      const grid         = document.getElementById('image-grid');
      const addBox       = document.getElementById('add-box');
      const hiddenBucket = document.getElementById('hidden-inputs');
      const toRemove     = document.getElementById('to-remove');
      const inputTpl     = document.getElementById('image-input-template');

      if (grid && addBox && hiddenBucket && toRemove && inputTpl) {
        let existingCount = Number(grid.dataset.existingCount || 0);

        const refresh = () => {
          // reindex ONLY new files
          const newInputs = hiddenBucket.querySelectorAll('input[type="file"]');
          newInputs.forEach((inp, i) => (inp.name = `image[${i}]`));

          const total = existingCount + newInputs.length;
          const atLimit = total >= maxImages;
          addBox.classList.toggle('d-none', atLimit);
          addBox.style.display = atLimit ? 'none' : 'flex';
        };

        const countNew = () =>
          hiddenBucket.querySelectorAll('input[type="file"]').length;

        const addPreviewForNew = (file, input) => {
          const wrap = document.createElement('div');
          wrap.className = 'img-wrapper position-relative';
          Object.assign(wrap.style, {
            width: '120px',
            height: '120px',
            overflow: 'hidden',
            borderRadius: '8px',
            border: '1px solid #ddd'
          });

          const img = document.createElement('img');
          img.src = URL.createObjectURL(file);
          img.alt = file.name;
          Object.assign(img.style, { width: '100%', height: '100%', objectFit: 'cover' });
          img.onload = () => URL.revokeObjectURL(img.src);
          wrap.appendChild(img);

          const btn = document.createElement('button');
          btn.type = 'button';
          btn.textContent = '×';
          btn.className = 'btn btn-sm btn-danger position-absolute top-0 end-0 m-1 p-0';
          Object.assign(btn.style, { width: '24px', height: '24px', borderRadius: '50%', lineHeight: '1' });
          btn.addEventListener('click', () => { wrap.remove(); input.remove(); refresh(); });
          wrap.appendChild(btn);

          grid.insertBefore(wrap, addBox);
        };

        // delete an existing image (already in DB)
        grid.addEventListener('click', (e) => {
          const btn = e.target.closest('.remove-existing');
          if (!btn) return;

          const wrap = btn.closest('.img-wrapper');
          if (!wrap) return;

          const name = wrap.dataset.name; // filename used as identifier
          if (name) {
            const rem = document.createElement('input');
            rem.type  = 'hidden';
            rem.name  = 'removeImageNames'; // controller: List<String> removeImageNames
            rem.value = name;
            toRemove.appendChild(rem);
          }

          wrap.remove();
          existingCount = Math.max(0, existingCount - 1);
          refresh();
        });

        // add a new image
        addBox.addEventListener('click', () => {
          const newCount = countNew();
          if (existingCount + newCount >= maxImages) { refresh(); return; }

          const input = inputTpl.cloneNode(true);
          input.classList.add('d-none');    // keep hidden even if Bootstrap CSS missing
          input.removeAttribute('id');
          input.name = `image[${newCount}]`; // Spring binds to MultipartFile[] image

          input.addEventListener('change', () => {
            if (!input.files || !input.files[0]) { 
              input.remove(); 
              refresh(); 
              return; 
            }
            addPreviewForNew(input.files[0], input);
            refresh();
          });

          hiddenBucket.appendChild(input);
          input.click();
        });

        // initial refresh (replace prior DOMContentLoaded used by this section)
        refresh();
      }
    }
	// =========================
	// 9) Basic image picker + drag & drop previews
	// =========================
	{
	  function initUploader(root) {
	    if (root.dataset.uploaderInit === '1') return; // ✅ guard against double init
	    root.dataset.uploaderInit = '1';

	    const box     = root.querySelector('[data-role="dropzone"]');
	    const input   = root.querySelector('[data-role="input"]');
	    const preview = root.querySelector('[data-role="preview"]');
	    if (!box || !input || !preview) return;

	    const uniqueKey = (f) => `${f.name}::${f.size}::${f.lastModified}`;

	    const renderPreviews = (files) => {
	      preview.innerHTML = '';
	      [...files].forEach((file) => {
	        if (!file?.type?.startsWith('image/')) return;

	        const url  = URL.createObjectURL(file);
	        const wrap = document.createElement('div');
	        wrap.className = 'preview-wrapper position-relative';
	        Object.assign(wrap.style, {
	          width:'240px', height:'240px', border:'1px solid #ddd',
	          borderRadius:'8px', overflow:'hidden'
	        });

	        const img = document.createElement('img');
	        img.className = 'preview-image';
	        Object.assign(img.style, { width:'100%', height:'100%', objectFit:'cover' });
	        img.alt = file.name || 'image';
	        img.src = url;
	        img.addEventListener('load', () => URL.revokeObjectURL(url), { once:true });

	        const btn = document.createElement('button');
	        btn.type = 'button';
	        btn.textContent = '×';
	        btn.className = 'btn btn-sm btn-danger position-absolute top-0 end-0 m-1 p-0';
	        Object.assign(btn.style, { width:'22px', height:'22px', lineHeight:'1', borderRadius:'50%' });
	        btn.addEventListener('click', () => {
	          // rebuild FileList without this file
	          const dt = new DataTransfer();
	          [...input.files].forEach((f) => { if (f !== file) dt.items.add(f); });
	          input.files = dt.files;
	          wrap.remove();
	        });

	        wrap.append(img, btn);
	        preview.appendChild(wrap);
	      });
	    };

	    // Open picker on click ONLY if the dropzone is NOT a LABEL (labels auto-trigger their input)
	    if (box.tagName !== 'LABEL') {
	      box.addEventListener('click', (e) => {
	        // avoid double when clicking directly on the hidden input
	        if (e.target === input) return;
	        input.click();
	      });
	    }

	    // Picker change (single source of truth)
	    input.addEventListener('change', () => {
	      renderPreviews(input.files);
	    });

	    // Drag & drop UX
	    const prevent = (e) => { e.preventDefault(); e.stopPropagation(); };
	    const hilite  = (e) => { prevent(e); box.classList.add('border-info','bg-light'); };
	    const unhi    = (e) => { prevent(e); box.classList.remove('border-info','bg-light'); };

	    ['dragenter','dragover'].forEach((evt) => box.addEventListener(evt, hilite));
	    ['dragleave','drop'].forEach((evt) => box.addEventListener(evt, unhi));

	    box.addEventListener('drop', (e) => {
	      prevent(e);
	      const files = e.dataTransfer?.files;
	      if (!files || !files.length) return;

	      // Merge & dedupe existing + new
	      const dt = new DataTransfer();
	      const seen = new Set();

	      [...input.files, ...files].forEach((f) => {
	        const key = uniqueKey(f);
	        if (!seen.has(key)) { seen.add(key); dt.items.add(f); }
	      });

	      input.files = dt.files;
	      // ❌ no synthetic 'change' here (avoids double processing)
	      renderPreviews(input.files);
	    });
	  }

	  const boot = () => {
	    document.querySelectorAll('[data-component="image-uploader"]').forEach(initUploader);
	  };

	  if (document.readyState === 'loading') {
	    document.addEventListener('DOMContentLoaded', boot, { once: true });
	  } else {
	    boot();
	  }
	}
    // ==== end onReady ====
  });
})();