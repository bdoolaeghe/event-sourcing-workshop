package fr.soat.eventsourcing.impl.db;

import fr.soat.eventsourcing.api.*;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

public abstract class AbstractDbRepository<ENTITY_ID extends EntityId,
        ENTITY extends Entity<ENTITY_ID, EVENT_TYPE>,
        EVENT_TYPE extends Event<ENTITY>>
//        implements EventStoreRepository<ENTITY_ID, ENTITY>
{

    private final EventStore<ENTITY_ID, EVENT_TYPE> eventStore;

    public AbstractDbRepository(EventStore<ENTITY_ID, EVENT_TYPE> es) {
        this.eventStore = es;
    }

    public ENTITY save(ENTITY entity) {
        ENTITY_ID entityId = entity.getId();
        if (entityId == null) {
            entityId = newEntityId();
        }
        int version = entity.getVersion();
        List<EVENT_TYPE> transientEvents = entity.getEvents().subList(version, entity.getEvents().size());

        try {
            if (transientEvents.size() > 0) {
                eventStore.store(entityId, transientEvents, version);
            }
            return hydrate(entityId, entity.getEvents());
        } catch (DuplicateKeyException e) {
            // a concurrent update occurred
            throw new EventConcurrentUpdateException(entity, e);
        }
    }

    protected abstract ENTITY_ID newEntityId();

    protected String generateEntityId() {
        return String.valueOf(eventStore.newEntityId());
    }

    public ENTITY load(ENTITY_ID entityId) {
        List<EVENT_TYPE> events = eventStore.loadEvents(entityId);
        return hydrate(entityId, events);
    }

    protected abstract ENTITY create(ENTITY_ID entityId, int version);

    private ENTITY hydrate(ENTITY_ID entityId, List<EVENT_TYPE> events) {
        ENTITY entity = create(entityId, events.size());
        for (EVENT_TYPE event : events) {
            entity = event.applyOn(entity);
        }
        return entity;
    }

}
