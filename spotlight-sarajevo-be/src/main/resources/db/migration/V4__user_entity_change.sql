ALTER TABLE ss_user
    ADD COLUMN registration_type VARCHAR(255) NOT NULL,
    ADD COLUMN sys_email VARCHAR(255),
    ADD COLUMN sys_password VARCHAR(15),
    ADD COLUMN google_id VARCHAR(255),
    ADD COLUMN google_email VARCHAR(255);

ALTER TABLE ss_user_auth_details
       DROP COLUMN registration_type,
       DROP COLUMN sys_email,
       DROP COLUMN sys_password,
       DROP COLUMN google_id,
       DROP COLUMN google_email;