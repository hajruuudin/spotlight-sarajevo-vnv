ALTER TABLE IF EXISTS ss_spot
ALTER COLUMN small_description_bs TYPE VARCHAR(120),
ALTER COLUMN small_description_en TYPE VARCHAR(120),
ALTER COLUMN full_description_bs TYPE TEXT,
ALTER COLUMN full_description_en TYPE TEXT;

ALTER TABLE IF EXISTS ss_event
ALTER COLUMN small_description_bs TYPE VARCHAR(120),
ALTER COLUMN small_description_en TYPE VARCHAR(120),
ALTER COLUMN full_description_bs TYPE TEXT,
ALTER COLUMN full_description_en TYPE TEXT;