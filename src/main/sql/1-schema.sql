CREATE TABLE event
(
    event_id          SERIAL PRIMARY KEY,
    entity_id         TEXT NOT NULL,
    event_sequence_id INT  NOT NULL,
    event_type        TEXT NOT NULL,
    timestamp         TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    content           TEXT NOT NULL,
    CONSTRAINT entity_id_unique UNIQUE (entity_id, event_sequence_id)
);

CREATE INDEX event_entity_id ON event (entity_id);

CREATE TABLE entity
(
    entity_id TEXT PRIMARY KEY,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE entity_id_seq;

CREATE TABLE account
(
    spectator_id TEXT PRIMARY KEY,
    balance      INT NOT NULL
);
