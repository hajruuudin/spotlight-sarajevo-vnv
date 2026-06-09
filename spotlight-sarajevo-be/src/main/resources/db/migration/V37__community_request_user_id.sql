DROP VIEW IF EXISTS vw_community_requests;

CREATE OR REPLACE VIEW vw_community_requests AS
SELECT
    u.username AS user_name,
    u.id AS user_id,
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