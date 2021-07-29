package fr.soat.eventsourcing.impl.db;

import fr.soat.eventsourcing.api.Entity;
import fr.soat.eventsourcing.api.EntityId;
import fr.soat.eventsourcing.api.Event;

public interface EventStoreRepository<ENTITY_ID extends EntityId, ENTITY extends Entity<ENTITY_ID, EVENT_TYPE>, EVENT_TYPE extends Event<ENTITY>> {
//public interface EventStoreRepository<ENTITY_ID extends EntityId, ENTITY extends Entity<ENTITY_ID, ? extends Event<ENTITY>>> {
//public interface EventStoreRepository<ENTITY_ID, ENTITY> {
    ENTITY load(ENTITY_ID entityId);
    ENTITY save(ENTITY entity);
}
