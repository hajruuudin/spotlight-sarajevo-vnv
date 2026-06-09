-- Add geometry column to ss_transport_method for storing line coverage
ALTER TABLE ss_transport_method
ADD COLUMN geometry GEOMETRY;

-- Drop the transport method station table
DROP TABLE IF EXISTS ss_transport_method_station;

-- Create new table for transport method lines
CREATE TABLE ss_transport_method_line (
    id SERIAL PRIMARY KEY,
    operator_id INTEGER NOT NULL,
    line_start VARCHAR(255) NOT NULL,
    line_end VARCHAR(255) NOT NULL,
    line_number VARCHAR(50) NOT NULL,
    geometry GEOMETRY,
    transport_type_id INTEGER NOT NULL,
    CONSTRAINT fk_operator FOREIGN KEY (operator_id) REFERENCES ss_transport_method_operator(id) ON DELETE CASCADE,
    CONSTRAINT fk_transport_type FOREIGN KEY (transport_type_id) REFERENCES ss_transport_method(id) ON DELETE CASCADE
);
