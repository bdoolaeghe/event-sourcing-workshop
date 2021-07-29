package fr.soat.eventsourcing.impl.db;

import fr.soat.eventsourcing.api.Entity;
import fr.soat.eventsourcing.api.EntityId;
import fr.soat.eventsourcing.api.Event;
import fr.soat.eventsourcing.api.EventStore;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public abstract class AbstractRepository<ENTITY_ID extends EntityId,
        ENTITY extends Entity<ENTITY_ID, EVENT_TYPE>,
        EVENT_TYPE extends Event<ENTITY>> {

    private final EventStore<ENTITY_ID, EVENT_TYPE> eventStore;

    public AbstractRepository(EventStore<ENTITY_ID, EVENT_TYPE> es) {
        this.eventStore = es;
    }

    public ENTITY save(ENTITY entity) {
        ENTITY_ID entityId = entity.getId();
        if (entityId == null) {
            entityId = newEntityId();
        }
        eventStore.store(entityId, entity.getEvents());
        return load(entityId);
    }

    protected abstract ENTITY_ID newEntityId();

    protected String generateEntityId() {
        return String.valueOf(eventStore.newEntityId());
    }

    public ENTITY load(ENTITY_ID entityId) {
        List<EVENT_TYPE> events = eventStore.loadEvents(entityId);
        return hydrate(entityId, events);
    }

    protected abstract ENTITY create(ENTITY_ID entityId);

    private ENTITY hydrate(ENTITY_ID entityId, List<EVENT_TYPE> events) {
        ENTITY entity = create(entityId);
        for (EVENT_TYPE event : events) {
            entity = event.applyOn(entity);
        }
        return entity;
    }

}
