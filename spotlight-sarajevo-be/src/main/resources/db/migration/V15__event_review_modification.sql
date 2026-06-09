-- 1. drop FK if exists
ALTER TABLE ss_event_review
  DROP CONSTRAINT IF EXISTS fk_event_review_event;

-- 2. rename table
ALTER TABLE ss_event_review
  RENAME TO ss_event_organiser_reviews;

-- 3. rename columns (NO "IF EXISTS")
ALTER TABLE ss_event_organiser_reviews
  RENAME COLUMN user_event_quality TO user_organiser_quality;

ALTER TABLE ss_event_organiser_reviews
  RENAME COLUMN user_event_enjoyability TO user_organiser_enjoyability;

ALTER TABLE ss_event_organiser_reviews
  RENAME COLUMN user_event_atmosphere TO user_organiser_atmosphere;

ALTER TABLE ss_event_organiser_reviews
  RENAME COLUMN event_id TO organiser_id;

-- 4. add new FK
ALTER TABLE ss_event_organiser_reviews
  ADD CONSTRAINT fk_event_organiser_reviews_organiser
    FOREIGN KEY (organiser_id) REFERENCES ss_event_organiser(id);
