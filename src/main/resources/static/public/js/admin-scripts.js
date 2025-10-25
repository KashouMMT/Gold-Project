/*!
    * Start Bootstrap - SB Admin v7.0.7 (https://startbootstrap.com/template/sb-admin)
    * Copyright 2013-2023 Start Bootstrap
    * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
    */
    // 
// Scripts
// 

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

document.querySelectorAll('.toggle-input-wrapper').forEach(wrapper => {
      const dropdown = wrapper.querySelector('[data-type="dropdown"]');
      const input = wrapper.querySelector('[data-type="input"]');
      const addBtn = wrapper.querySelector('[data-type="add"]');
      const removeBtn = wrapper.querySelector('[data-type="remove"]');

	  // start state
	  dropdown.disabled = false;
	  input.disabled = true;
	  
      addBtn.addEventListener('click', () => {
        dropdown.classList.add('d-none');  dropdown.disabled = true;
        addBtn.classList.add('d-none');
        input.classList.remove('d-none'); input.disabled = false; input.focus();
        removeBtn.classList.remove('d-none');
        input.focus();
      });

      removeBtn.addEventListener('click', () => {
        input.classList.add('d-none'); input.disabled = true;  input.value = '';
        removeBtn.classList.add('d-none');
        dropdown.classList.remove('d-none'); dropdown.disabled = false;
        addBtn.classList.remove('d-none');
        input.value = '';
      });
});

document.addEventListener('DOMContentLoaded', () => {
  const widthsRoot = document.getElementById('widths-root');
  const tplWidth   = document.getElementById('tpl-width');
  const tplLength  = document.getElementById('tpl-length');
  const btnAddWidth= document.getElementById('js-add-width');

  function addWidthBlock() {
    const wIdx = widthsRoot.querySelectorAll('.width-block').length;
    const frag = document.createRange().createContextualFragment(
      tplWidth.innerHTML.replaceAll('__W__', wIdx)
    );
    widthsRoot.appendChild(frag);
    const block = widthsRoot.querySelectorAll('.width-block')[wIdx];
    addLengthRow(block, wIdx); // seed 1 length for brand-new width
  }

  function addLengthRow(block, wIdx) {
    const tbody = block.querySelector('tbody.lengths');
    const lIdx = tbody.querySelectorAll('tr').length;
    const html = tplLength.innerHTML
      .replaceAll('__W__', wIdx)
      .replaceAll('__L__', lIdx);
    const frag = document.createRange().createContextualFragment(html);
    tbody.appendChild(frag);
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
        tr.querySelectorAll('input[name]').forEach(inp => {
          if (inp.name.endsWith('.length'))  inp.name = `productWidthDto[${wIdx}].productLengthDto[${lIdx}].length`;
          else if (inp.name.endsWith('.weight')) inp.name = `productWidthDto[${wIdx}].productLengthDto[${lIdx}].weight`;
          else if (inp.name.endsWith('.price'))  inp.name = `productWidthDto[${wIdx}].productLengthDto[${lIdx}].price`;
          else if (inp.name.endsWith('.id'))     inp.name = `productWidthDto[${wIdx}].productLengthDto[${lIdx}].id`;
        });
      });

      // 👇 removed the auto-add to allow zero-lengths if user deleted last row
      // if (rows.length === 0) addLengthRow(block, wIdx);
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
});

(function () {
  const form     = document.getElementById('variantsForm');
  const widthEl  = document.getElementById('widthCount');
  const lengthEl = document.getElementById('lengthCount');
  const tbody    = document.querySelector('#variantsTable tbody');
  const buildBtn = document.getElementById('build');

  // Regex rules equivalent to @Digits(integer=5, fraction=2) and @Digits(integer=8, fraction=2)
  const RULES = {
    '5_2': /^\d{1,5}(\.\d{1,2})?$/,
    '8_2': /^\d{1,8}(\.\d{1,2})?$/
  };

  // Grab current values before rebuild, keyed by input name
  const snapshotValues = () => {
    const map = new Map();
    tbody.querySelectorAll('input[name]').forEach(inp => map.set(inp.name, inp.value));
    return map;
  };

  // Create an input with Bootstrap classes + validation metadata
  function createInput(name, { rule = '5_2', step = '0.01', min = '0', placeholder = name } = {}) {
    const input = document.createElement('input');
    input.type = 'text';               // use text so regex pattern is fully respected
    input.name = name;
    input.className = 'form-control';
    input.placeholder = placeholder;
    input.required = true;
    input.dataset.rule = rule;         // pick '5_2' or '8_2'
    input.dataset.step = step;         // for UI hint (optional)
    input.dataset.min = min;
    // optional hint shown on invalid
    input.title = rule === '8_2' ? 'Up to 8 digits and 2 decimals (e.g., 12345678.90)' :
                                   'Up to 5 digits and 2 decimals (e.g., 12345.67)';

    // add Bootstrap invalid feedback container
    const wrap = document.createElement('div');
    wrap.className = 'd-flex flex-column gap-1';
    const feedback = document.createElement('div');
    feedback.className = 'invalid-feedback';
    feedback.textContent = input.title;
    wrap.appendChild(input);
    wrap.appendChild(feedback);
    // return a wrapper so caller can append nicely
    return { input, wrapper: wrap };
  }

  // Validate a single input by its rule
  function validateField(input) {
    const ruleKey = input.dataset.rule || '5_2';
    const re = RULES[ruleKey];
    const val = (input.value || '').trim();
    const ok = val.length > 0 && re.test(val);
    input.classList.toggle('is-invalid', !ok);
    input.classList.toggle('is-valid', ok);
    return ok;
  }

  // Validate all generated inputs
  function validateAll() {
    let allOK = true;
    tbody.querySelectorAll('input[name]').forEach(inp => {
      if (!validateField(inp)) allOK = false;
    });
    return allOK;
  }

  // Render the table based on widthCount (W) and lengthCount (L)
  function renderTable() {
    const W = Math.max(0, parseInt(widthEl.value)  || 0);
    const L = Math.max(0, parseInt(lengthEl.value) || 0);
    const prev = snapshotValues();
    tbody.innerHTML = '';

    if (W === 0 || L === 0) return;

    for (let i = 0; i < W; i++) {
      for (let j = 0; j < L; j++) {
        const tr = document.createElement('tr');

        // First row in a width group: show width[i] and make it span L rows
        if (j === 0) {
          const tdWidth = document.createElement('td');
          tdWidth.rowSpan = L;
          const widthName = `width[${i}]`;
          const { input: widthInput, wrapper: widthWrap } = createInput(widthName, { rule: '5_2' });
          if (prev.has(widthName)) widthInput.value = prev.get(widthName);
          tdWidth.appendChild(widthWrap);
          tr.appendChild(tdWidth);
        }

        // Length (i, j) → rule 5_2
        const tdLength = document.createElement('td');
        const lengthName = `length[${i}][${j}]`;
        const { input: lengthInput, wrapper: lengthWrap } = createInput(lengthName, { rule: '5_2' });
        if (prev.has(lengthName)) lengthInput.value = prev.get(lengthName);
        tdLength.appendChild(lengthWrap);
        tr.appendChild(tdLength);

        // Price (i, j) → rule 8_2
        const tdPrice = document.createElement('td');
        const priceName = `price[${i}][${j}]`;
        const { input: priceInput, wrapper: priceWrap } = createInput(priceName, { rule: '8_2' });
        if (prev.has(priceName)) priceInput.value = prev.get(priceName);
        tdPrice.appendChild(priceWrap);
        tr.appendChild(tdPrice);

        tbody.appendChild(tr);
      }
    }

    // initial validation pass (optional)
    validateAll();
  }

  // Build button: rebuild the table
  buildBtn.addEventListener('click', renderTable);

  // Rebuild when counts change
  widthEl.addEventListener('change', renderTable);
  lengthEl.addEventListener('change', renderTable);

  // Live validation as user types (event delegation)
  tbody.addEventListener('input', (e) => {
    const target = e.target;
    if (target && target.name) validateField(target);
  });

  // Block submit if any field invalid
  form.addEventListener('submit', (e) => {
    if (!validateAll()) {
      e.preventDefault();
      e.stopPropagation();
      // optional toast/alert
      // alert('Please correct invalid numeric fields.');
    }
  });

  // Initial render
  renderTable();
})();

document.getElementById('variantsForm').addEventListener('submit', e => {
  const fd = new FormData(e.target);
  // list keys/values going to the server
  console.log('--- SUBMIT KEYS ---');
  for (const [k, v] of fd.entries()) console.log(k, '=', v);
});

document.getElementById('variantsForm').addEventListener('submit', e => {
  const hasWidth = !!document.querySelector('#variantsTable tbody input[name^="width["]');
  const hasLen   = !!document.querySelector('#variantsTable tbody input[name^="length["]');
  const hasPrice = !!document.querySelector('#variantsTable tbody input[name^="price["]');
  if (!hasWidth || !hasLen || !hasPrice) {
    e.preventDefault();
    alert('Please generate the table first.');
  }
});

(function () {
  const form = document.getElementById('variantsForm');
  if (!form) return;

  form.addEventListener('submit', (e) => {
    // --- build a FormData snapshot ---
    const fd = new FormData(form);

    // 1) Dump every key/value
    console.log('===== FORM SUBMIT: raw entries =====');
    for (const [k, v] of fd.entries()) {
      console.log(k, '=', v);
    }

    // 2) Group by families we care about
    const groups = { width: [], length: [], price: [], other: [] };
    for (const [k, v] of fd.entries()) {
      if (k.startsWith('width['))   groups.width.push([k, v]);
      else if (k.startsWith('length[')) groups.length.push([k, v]);
      else if (k.startsWith('price['))  groups.price.push([k, v]);
      else groups.other.push([k, v]);
    }

    console.log('===== COUNTS =====');
    console.table({
      width:  groups.width.length,
      length: groups.length.length,
      price:  groups.price.length,
      other:  groups.other.length
    });

    console.log('===== width[] (first 10) =====');
    console.table(groups.width.slice(0, 10));
    console.log('===== length[][] (first 10) =====');
    console.table(groups.length.slice(0, 10));
    console.log('===== price[][] (first 10) =====');
    console.table(groups.price.slice(0, 10));

    // 3) Quick sanity checks
    if (groups.width.length === 0)  console.warn('⚠️ No width[...] fields found!');
    if (groups.length.length === 0) console.warn('⚠️ No length[...][...] fields found!');
    if (groups.price.length === 0)  console.warn('⚠️ No price[...][...] fields found!');

    // 4) (Optional) see the exact payload as a query string
    const qp = new URLSearchParams(fd);
    console.log('===== URL-encoded payload =====');
    console.log(qp.toString());

    // NOTE: this does NOT block submit; it just logs.
    // If you want to block submit while debugging, uncomment:
    // e.preventDefault();
  });
})();