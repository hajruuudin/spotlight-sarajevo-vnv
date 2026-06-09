-- Drop old foreign keys
ALTER TABLE ss_user_favourite_spots
DROP CONSTRAINT fk_user_favourite_spots_spot_id;

ALTER TABLE ss_user_favourite_events
DROP CONSTRAINT fk_user_favourite_events_event_id;

-- Add new foreign keys
ALTER TABLE ss_user_favourite_spots
ADD CONSTRAINT fk_user_favourite_spots_spot_category_id
FOREIGN KEY (spot_id) REFERENCES ss_spot_category(id);

ALTER TABLE ss_user_favourite_events
ADD CONSTRAINT fk_user_favourite_events_event_category_id
FOREIGN KEY (event_id) REFERENCES ss_event_category(id);

