ALTER TABLE IF EXISTS ss_tourist_guide
    ADD COLUMN IF NOT EXISTS guide_full_description_en TEXT NOT NULL;

ALTER TABLE IF EXISTS ss_tourist_guide
    ADD COLUMN IF NOT EXISTS guide_full_description_bs TEXT NOT NULL;