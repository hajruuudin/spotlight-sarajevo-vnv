-- 1. Create the new Category Table first
CREATE TABLE ss_tourist_guide_category (
    id SERIAL PRIMARY KEY,
    category_name_bs VARCHAR(100) NOT NULL,
    category_name_en VARCHAR(100) NOT NULL,
    category_desc_bs TEXT,
    category_desc_en TEXT,
    color_code VARCHAR(7) NOT NULL
);

-- 2. Modify ss_tourist_guide
ALTER TABLE ss_tourist_guide DROP COLUMN guide_title;

ALTER TABLE ss_tourist_guide
    ADD COLUMN guide_title_bs VARCHAR(100) NOT NULL DEFAULT '',
    ADD COLUMN guide_title_en VARCHAR(100) NOT NULL DEFAULT '',
    ADD COLUMN guide_type VARCHAR(20) DEFAULT 'SYSTEM' CHECK (guide_type IN ('SYSTEM', 'EXTERNAL')),
    ADD COLUMN category_id INTEGER REFERENCES ss_tourist_guide_category(id) ON DELETE SET NULL,
    ADD COLUMN contact_info JSONB,
    ALTER COLUMN guide_small_description_bs TYPE VARCHAR(250),
    ALTER COLUMN guide_small_description_en TYPE VARCHAR(250);

-- 3. Modify ss_tourist_guide_section
ALTER TABLE ss_tourist_guide_section
    ADD COLUMN order_idx INTEGER DEFAULT 0,
    ALTER COLUMN section_title_bs TYPE VARCHAR(100),
    ALTER COLUMN section_title_en TYPE VARCHAR(100);

-- 4. Update ss_media_store check constraint
ALTER TABLE ss_media_store DROP CONSTRAINT ss_media_store_item_category;

ALTER TABLE ss_media_store
    ADD CONSTRAINT ss_media_store_item_category
    CHECK (item_category::text = ANY (ARRAY[
        'SPOT', 'EVENT', 'SECTION_GUIDE', 'TRANSPORT', 'ORGANISER', 'GUIDE_THUMBNAIL'
    ]::text[]));

---
--- SEED DATA: 10 Categories
---
INSERT INTO ss_tourist_guide_category (category_name_bs, category_name_en, category_desc_bs, category_desc_en, color_code)
VALUES
('Historija i Kultura', 'History & Culture', 'Otkrijte bogatu prošlost Sarajeva.', 'Discover the rich past of Sarajevo.', '#E0FFFF'),
('Gastronomija', 'Gastronomy', 'Najbolja mjesta za jelo i piće.', 'The best places to eat and drink.', '#D1FFBD'),
('Priroda i Planine', 'Nature & Mountains', 'Planine oko grada i izletišta.', 'Mountains around the city and picnic spots.', '#90EE90'),
('Religija', 'Religion', 'Vjerski objekti i tradicija.', 'Religious sites and traditions.', '#AFEEEE'),
('Arhitektura', 'Architecture', 'Spoj istoka i zapada kroz građevine.', 'The blend of East and West through buildings.', '#00CED1'),
('Noćni Život', 'Nightlife', 'Barovi, klubovi i zabava.', 'Bars, clubs, and entertainment.', '#00FFFF'),
('Tradicija i Zanati', 'Tradition & Crafts', 'Stari zanati na Baščaršiji.', 'Old crafts in Baščaršija.', '#ADFF2F'),
('Ratno Sarajevo', 'War & Siege', 'Lokacije bitne za opsadu Sarajeva.', 'Locations significant to the Siege of Sarajevo.', '#B0E0E6'),
('Aktivni Odmor', 'Active Holiday', 'Planinarenje, biciklizam i sport.', 'Hiking, cycling, and sports.', '#7FFFD4');