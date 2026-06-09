-- Version 26: i onda odma skonto da sam sjebo
CREATE OR REPLACE VIEW ss_spot_review_stats AS
SELECT
    s.id AS spot_id,
    COALESCE(AVG(r.user_overall_rating), 0) AS combined_rating,
    COALESCE(AVG(r.user_cleanliness), 0) AS combined_cleanliness,
    COALESCE(AVG(r.user_affordability), 0) AS combined_affordability,
    COALESCE(AVG(r.user_accessibility), 0) AS combined_accessibility,
    COALESCE(AVG(r.user_staff_kindness), 0) AS combined_staff_kindness,
    COALESCE(AVG(r.user_locale_quality), 0) AS combined_quality,
    COALESCE(AVG(r.user_atmosphere), 0) AS combined_atmosphere
FROM ss_spot s
LEFT JOIN ss_spot_review r ON s.id = r.spot_id
GROUP BY s.id;