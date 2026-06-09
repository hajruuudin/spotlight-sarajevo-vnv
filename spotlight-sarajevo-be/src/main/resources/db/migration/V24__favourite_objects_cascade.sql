-- Version 24: Favourite objects table didn't have ON DELETE CASCADE on the user foreign key, so that's what this adds:
ALTER TABLE ss_user_favourite_events
    ADD CONSTRAINT fk_favourite_events_user
        FOREIGN KEY (user_id)
        REFERENCES ss_user(id)
        ON DELETE CASCADE;

ALTER TABLE ss_user_favourite_spots
    ADD CONSTRAINT fk_favourite_spots_user
        FOREIGN KEY (user_id)
        REFERENCES ss_user(id)
        ON DELETE CASCADE;