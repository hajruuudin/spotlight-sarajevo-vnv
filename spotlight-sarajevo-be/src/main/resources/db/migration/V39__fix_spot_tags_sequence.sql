-- =====================================================
-- Fix all sequences to prevent duplicate key errors
-- This resolves sequence issues when data was inserted with explicit IDs
-- =====================================================

-- Core entity tables
SELECT setval(pg_get_serial_sequence('ss_spot', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_spot), 0) + 1, false);

SELECT setval(pg_get_serial_sequence('ss_event', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_event), 0) + 1, false);

SELECT setval(pg_get_serial_sequence('ss_user', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_user), 0) + 1, false);

-- Category and tag tables
SELECT setval(pg_get_serial_sequence('ss_spot_category', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_spot_category), 0) + 1, false);

SELECT setval(pg_get_serial_sequence('ss_event_category', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_event_category), 0) + 1, false);

SELECT setval(pg_get_serial_sequence('ss_tag', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_tag), 0) + 1, false);

-- Junction/intermediary tables
SELECT setval(pg_get_serial_sequence('ss_spot_tags', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_spot_tags), 0) + 1, false);

SELECT setval(pg_get_serial_sequence('ss_event_tags', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_event_tags), 0) + 1, false);

SELECT setval(pg_get_serial_sequence('ss_spot_work_hours', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_spot_work_hours), 0) + 1, false);

SELECT setval(pg_get_serial_sequence('ss_collection', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_collection), 0) + 1, false);

SELECT setval(pg_get_serial_sequence('ss_collection_spot', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_collection_spot), 0) + 1, false);

SELECT setval(pg_get_serial_sequence('ss_collection_event', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_collection_event), 0) + 1, false);

-- User related tables
SELECT setval(pg_get_serial_sequence('ss_user_attended_events', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_user_attended_events), 0) + 1, false);

SELECT setval(pg_get_serial_sequence('ss_user_visited_spots', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_user_visited_spots), 0) + 1, false);

-- Review tables
SELECT setval(pg_get_serial_sequence('ss_spot_review', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_spot_review), 0) + 1, false);

SELECT setval(pg_get_serial_sequence('ss_event_organiser_reviews', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_event_organiser_reviews), 0) + 1, false);

-- Media and content tables
SELECT setval(pg_get_serial_sequence('ss_media_store', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_media_store), 0) + 1, false);

SELECT setval(pg_get_serial_sequence('ss_spot_contact', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_spot_contact), 0) + 1, false);

-- Event organiser tables
SELECT setval(pg_get_serial_sequence('ss_event_organiser', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_event_organiser), 0) + 1, false);

-- Community request tables
SELECT setval(pg_get_serial_sequence('ss_community_request', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_community_request), 0) + 1, false);

SELECT setval(pg_get_serial_sequence('ss_community_request_pending_info', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_community_request_pending_info), 0) + 1, false);

-- Tourist guide tables
SELECT setval(pg_get_serial_sequence('ss_tourist_guide', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_tourist_guide), 0) + 1, false);

SELECT setval(pg_get_serial_sequence('ss_tourist_guide_section', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_tourist_guide_section), 0) + 1, false);

SELECT setval(pg_get_serial_sequence('ss_tourist_guide_category', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_tourist_guide_category), 0) + 1, false);

-- Transport tables
SELECT setval(pg_get_serial_sequence('ss_transport_method', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_transport_method), 0) + 1, false);

SELECT setval(pg_get_serial_sequence('ss_transport_method_operator', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_transport_method_operator), 0) + 1, false);

SELECT setval(pg_get_serial_sequence('ss_transport_method_line', 'id'),
              COALESCE((SELECT MAX(id) FROM ss_transport_method_line), 0) + 1, false);

-- Note: ss_transport_taxi_info uses INTEGER PRIMARY KEY (not SERIAL), so no sequence to reset
-- Note: ss_event_review was dropped and renamed to ss_event_organiser_reviews
-- Note: ss_transport_method_station was dropped in V32


