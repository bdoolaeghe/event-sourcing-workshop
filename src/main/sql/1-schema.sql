CREATE TABLE event
(
    id                SERIAL PRIMARY KEY,
    aggregate_id      INT  NOT NULL,
    event_sequence_id INT  NOT NULL,
    event_type        TEXT NOT NULL,
    timestamp         TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    content           TEXT NOT NULL,
    CONSTRAINT aggregate_id_unique UNIQUE (aggregate_id, event_sequence_id)
);
