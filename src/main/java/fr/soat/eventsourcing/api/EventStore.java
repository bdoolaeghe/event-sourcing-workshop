package fr.soat.eventsourcing.api;

import java.util.List;

public interface EventStore<ENTITY_ID extends EntityId, EVENT_TYPE extends Event> {

    List<EVENT_TYPE> loadEvents(ENTITY_ID entityId);
    void store(ENTITY_ID entityId, List<EVENT_TYPE> events);
    int newEntityId();

}
