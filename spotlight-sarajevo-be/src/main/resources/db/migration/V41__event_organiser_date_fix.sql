ALTER TABLE ss_event_organiser
DROP COLUMN organiser_creation_date;

ALTER TABLE ss_event_organiser
ADD COLUMN organiser_creation_date DATE DEFAULT CURRENT_DATE;