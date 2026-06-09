-- Version 25: The initial values of spot ratings are stuped as admins should not have to know about the spot
-- interior to add it. Makes the process simpler
CREATE OR REPLACE VIEW ss_spot_review_stats AS
SELECT
    r.spot_id,
    AVG(r.user_overall_rating) AS combined_rating,
    AVG(r.user_cleanliness) AS combined_cleanliness,
    AVG(r.user_affordability) AS combined_affordability,
    AVG(r.user_accessibility) AS combined_accessibility,
    AVG(r.user_staff_kindness) AS combined_staff_kindness,
    AVG(r.user_locale_quality) AS combined_quality,
    AVG(r.user_atmosphere) AS combined_atmosphere
FROM
    ss_spot_review r
GROUP BY
    r.spot_id;


ALTER VIEW ss_spot_review_stats OWNER TO "hajrudin.imamovic";

ALTER TABLE IF EXISTS ss_spot
    DROP COLUMN IF EXISTS initial_overall_rating;

ALTER TABLE IF EXISTS ss_spot
    DROP COLUMN IF EXISTS initial_staff_kindness;

ALTER TABLE IF EXISTS ss_spot
    DROP COLUMN IF EXISTS initial_atmosphere;

ALTER TABLE IF EXISTS ss_spot
    DROP COLUMN IF EXISTS initial_cleanliness;

ALTER TABLE IF EXISTS ss_spot
    DROP COLUMN IF EXISTS initial_affordability;

ALTER TABLE IF EXISTS ss_spot
    DROP COLUMN IF EXISTS initial_accessibility;

ALTER TABLE IF EXISTS ss_spot
    DROP COLUMN IF EXISTS initial_locale_quality;