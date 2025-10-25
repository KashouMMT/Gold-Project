-- =========================================
-- Test Data Seed for Categories, Products, Variations
-- =========================================

SET FOREIGN_KEY_CHECKS = 0;

-- If tables already exist with data, clear in child->parent order
TRUNCATE TABLE variations;
TRUNCATE TABLE products;
TRUNCATE TABLE categories;

SET FOREIGN_KEY_CHECKS = 1;

-- =========================================
-- 1) CATEGORIES
-- =========================================
INSERT INTO categories (category_name, description)
VALUES
  ('Rings',       'Silver rings for daily wear and ritual use'),
  ('Necklaces',   'Sterling silver chains and pendants'),
  ('Bracelets',   'Silver bracelets in minimal and vintage styles'),
  ('Pendants',    'Symbolic and spiritual silver pendants');

-- =========================================
-- 2) PRODUCTS
--   - material: 925 Sterling Silver
--   - theme: Spiritual / Japanese / Minimal
--   - occasion: Daily Wear / Gift / Ceremony
--   - product_image: example paths or CDN URLs
--   - category_id: resolved via subquery on category_name
-- =========================================

-- Necklaces
INSERT INTO products (title, description, material, theme, occasion, product_image, category_id)
VALUES
  (
    'Ginou – Cable Angle Chain Necklace',
    '925 silver Cable Angle Chain with elegant angled links; great for daily wear.',
    '925 Sterling Silver', 'Spiritual', 'Daily Wear',
    '/images/necklaces/cable-angle-45.jpg',
    (SELECT id FROM categories WHERE category_name = 'Necklaces')
  ),
  (
    'Valentino Sun Chain Necklace',
    'Bright reflective pattern inspired by sun-ray motif; minimal yet striking.',
    '925 Sterling Silver', 'Japanese', 'Gift',
    '/images/necklaces/valentino-sun-50.jpg',
    (SELECT id FROM categories WHERE category_name = 'Necklaces')
  );

-- Rings
INSERT INTO products (title, description, material, theme, occasion, product_image, category_id)
VALUES
  (
    'Moon Cleansing Ring',
    'Smooth silver band intended for simple nightly cleansing ritual.',
    '925 Sterling Silver', 'Spiritual', 'Daily Wear',
    '/images/rings/moon-cleansing.jpg',
    (SELECT id FROM categories WHERE category_name = 'Rings')
  ),
  (
    'Phoenix Rebirth Signet',
    'Oxidized surface with phoenix outline—symbol of renewal and protection.',
    '925 Sterling Silver', 'Japanese', 'Ceremony',
    '/images/rings/phoenix-rebirth.jpg',
    (SELECT id FROM categories WHERE category_name = 'Rings')
  );

-- Pendants
INSERT INTO products (title, description, material, theme, occasion, product_image, category_id)
VALUES
  (
    'Spirit Shield Pendant',
    'Flat round pendant with subtle etched pattern; pairs with thin chains.',
    '925 Sterling Silver', 'Spiritual', 'Gift',
    '/images/pendants/spirit-shield.jpg',
    (SELECT id FROM categories WHERE category_name = 'Pendants')
  );

-- Bracelets
INSERT INTO products (title, description, material, theme, occasion, product_image, category_id)
VALUES
  (
    'Minimal Cable Bracelet',
    'Lightweight cable bracelet with clean polish.',
    '925 Sterling Silver', 'Minimal', 'Daily Wear',
    '/images/bracelets/minimal-cable.jpg',
    (SELECT id FROM categories WHERE category_name = 'Bracelets')
  );

-- =========================================
-- 3) VARIATIONS (size, thickness, price)
--    Assumptions:
--      - size: for necklaces/bracelets -> cm (e.g., 40.00, 45.00)
--              for rings -> inner diameter or ring size unit (example uses 16.00, 17.00)
--      - thickness: mm (e.g., 1.50, 2.00)
--      - price: in THB (example values)
--    Ensure unique combinations per product to avoid unique constraint conflicts.
-- =========================================

-- Variations for: Ginou – Cable Angle Chain Necklace
INSERT INTO variations (size, thickness, price, product_id)
VALUES
  (40.00, 1.50,  690.00, (SELECT id FROM products WHERE title = 'Ginou – Cable Angle Chain Necklace')),
  (45.00, 1.50,  740.00, (SELECT id FROM products WHERE title = 'Ginou – Cable Angle Chain Necklace')),
  (50.00, 1.50,  790.00, (SELECT id FROM products WHERE title = 'Ginou – Cable Angle Chain Necklace')),
  (55.00, 2.00,  890.00, (SELECT id FROM products WHERE title = 'Ginou – Cable Angle Chain Necklace')),
  (60.00, 2.00,  950.00, (SELECT id FROM products WHERE title = 'Ginou – Cable Angle Chain Necklace'));

-- Variations for: Valentino Sun Chain Necklace
INSERT INTO variations (size, thickness, price, product_id)
VALUES
  (45.00, 2.00,  920.00, (SELECT id FROM products WHERE title = 'Valentino Sun Chain Necklace')),
  (50.00, 2.00,  980.00, (SELECT id FROM products WHERE title = 'Valentino Sun Chain Necklace')),
  (55.00, 2.50, 1090.00, (SELECT id FROM products WHERE title = 'Valentino Sun Chain Necklace')),
  (60.00, 2.50, 1190.00, (SELECT id FROM products WHERE title = 'Valentino Sun Chain Necklace'));

-- Variations for: Moon Cleansing Ring
-- Using ring "size" as ring size unit (e.g., 16.00, 17.00) and thickness as band thickness in mm
INSERT INTO variations (size, thickness, price, product_id)
VALUES
  (16.00, 2.00,  590.00, (SELECT id FROM products WHERE title = 'Moon Cleansing Ring')),
  (17.00, 2.00,  590.00, (SELECT id FROM products WHERE title = 'Moon Cleansing Ring')),
  (18.00, 2.50,  640.00, (SELECT id FROM products WHERE title = 'Moon Cleansing Ring'));

-- Variations for: Phoenix Rebirth Signet
INSERT INTO variations (size, thickness, price, product_id)
VALUES
  (17.00, 2.50,  980.00, (SELECT id FROM products WHERE title = 'Phoenix Rebirth Signet')),
  (18.00, 2.50,  980.00, (SELECT id FROM products WHERE title = 'Phoenix Rebirth Signet')),
  (19.00, 3.00, 1090.00, (SELECT id FROM products WHERE title = 'Phoenix Rebirth Signet'));

-- Variations for: Spirit Shield Pendant
-- Pendants: use size as diameter or default chain length offering
INSERT INTO variations (size, thickness, price, product_id)
VALUES
  (40.00, 1.20,  750.00, (SELECT id FROM products WHERE title = 'Spirit Shield Pendant')),
  (45.00, 1.20,  790.00, (SELECT id FROM products WHERE title = 'Spirit Shield Pendant'));

-- Variations for: Minimal Cable Bracelet
INSERT INTO variations (size, thickness, price, product_id)
VALUES
  (16.00, 1.50,  520.00, (SELECT id FROM products WHERE title = 'Minimal Cable Bracelet')),
  (18.00, 1.50,  540.00, (SELECT id FROM products WHERE title = 'Minimal Cable Bracelet')),
  (20.00, 2.00,  590.00, (SELECT id FROM products WHERE title = 'Minimal Cable Bracelet'));
