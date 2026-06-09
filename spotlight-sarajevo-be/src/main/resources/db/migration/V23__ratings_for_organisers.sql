-- Remove unnecessary columns from organiser reviews table
ALTER TABLE IF EXISTS ss_event_organiser_reviews
    DROP COLUMN IF EXISTS user_event_quality;

ALTER TABLE IF EXISTS ss_event_organiser_reviews
    DROP COLUMN IF EXISTS user_event_atmosphere;

ALTER TABLE IF EXISTS ss_event_organiser_reviews
    DROP COLUMN IF EXISTS user_event_enjoyability;

-- Add the rating column to the event organiser review
ALTER TABLE IF EXISTS ss_event_organiser_reviews
    ADD COLUMN IF NOT EXISTS user_overall_rating NUMERIC NOT NULL;

-- Create a view based on the average ratings for the reviews
-- Calculated as the arithmetic middle of all the reviews
CREATE OR REPLACE VIEW ss_organiser_review_stats AS
SELECT
    organiser_id,
    AVG(user_overall_rating) AS combined_rating,
    AVG(user_organiser_atmosphere) AS combined_atmosphere,
    AVG(user_organiser_enjoyability) AS combined_enjoyability,
    AVG(user_organiser_quality) AS combined_quality
FROM
    ss_event_organiser_reviews
GROUP BY
    organiser_id;