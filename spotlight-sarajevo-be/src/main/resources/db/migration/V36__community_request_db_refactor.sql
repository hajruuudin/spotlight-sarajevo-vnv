DROP TABLE IF EXISTS ss_community_request CASCADE;

CREATE TYPE request_type_enum AS ENUM ('ADD', 'UPDATE', 'REMOVE', 'OTHER');
CREATE TYPE object_type_enum AS ENUM ('SPOT', 'EVENT');
CREATE TYPE request_status_enum AS ENUM ('PENDING', 'APPROVED', 'REJECTED');

-- New community request table
CREATE TABLE ss_community_request (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    request_type request_type_enum NOT NULL,
    object_type object_type_enum NOT NULL,
    request_header VARCHAR(255) NOT NULL,
    request_description TEXT NOT NULL,
    status request_status_enum NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_community_request_user_id
        FOREIGN KEY (user_id)
        REFERENCES ss_user(id)
        ON DELETE CASCADE
);

-- Table for keeping pending information about spots or events
CREATE TABLE ss_community_request_pending_info (
    id SERIAL PRIMARY KEY,
    community_request_id INT NOT NULL,
    pending_info JSONB NOT NULL,

    CONSTRAINT fk_pending_info_community_request_id
        FOREIGN KEY (community_request_id)
        REFERENCES ss_community_request(id)
        ON DELETE CASCADE
);
-- Index to speed up lookups by user_id
CREATE INDEX idx_community_request_user_id ON ss_community_request(user_id);

-- View to easily access pending community requests with user info
CREATE OR REPLACE VIEW vw_community_requests AS
SELECT
    u.username AS user_name,
    cr.request_type,
    cr.object_type,
    cr.request_header,
    cr.request_description,
    cr.status,
    cr.created_at,
    pr.pending_info
FROM ss_community_request cr
JOIN ss_user u ON cr.user_id = u.id
LEFT JOIN ss_community_request_pending_info pr ON cr.id = pr.community_request_id;