-- For ss_spot table
ALTER TABLE ss_spot
ADD COLUMN IF NOT EXISTS official_name_bs VARCHAR(255);

ALTER TABLE ss_spot
RENAME COLUMN official_name TO official_name_en;

-- For ss_events table
ALTER TABLE ss_event
ADD COLUMN IF NOT EXISTS official_name_bs VARCHAR(255);

ALTER TABLE ss_event
RENAME COLUMN official_name TO official_name_en;