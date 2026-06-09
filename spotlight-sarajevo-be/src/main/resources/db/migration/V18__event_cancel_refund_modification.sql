ALTER TABLE ss_event
ALTER COLUMN cancel_refund TYPE BOOLEAN
USING (cancel_refund::text::boolean);