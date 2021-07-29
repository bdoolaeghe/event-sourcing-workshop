CREATE TABLE event
(
    event_id                SERIAL PRIMARY KEY,
    entity_id      TEXT  NOT NULL,
    event_sequence_id INT  NOT NULL,
    event_type        TEXT NOT NULL,
    timestamp         TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    content           TEXT NOT NULL,
    CONSTRAINT entity_id_unique UNIQUE (entity_id, event_sequence_id)
);

CREATE SEQUENCE entity_id_seq;
