ALTER TABLE IF EXISTS ss_user_attended_events
    DROP CONSTRAINT IF EXISTS fk_user_attended_events_event_id;

ALTER TABLE IF EXISTS ss_user_attended_events
    ADD CONSTRAINT fk_user_attended_events_event_id
    FOREIGN KEY (event_id)
    REFERENCES ss_event(id)
    ON DELETE CASCADE;
