package fr.soat.eventsourcing.api;

import org.springframework.dao.DuplicateKeyException;

public class EventConcurrentUpdateException extends RuntimeException {
    public EventConcurrentUpdateException(String msg) {
        super(msg);
    }

    public EventConcurrentUpdateException(Entity<?, ?> entity, DuplicateKeyException e) {
        super(entity.getClass().getSimpleName() + " (id='" + entity.getId() + "') has been meanwhile concurrently updated. Can not save this concurrent version. Refresh before update and try again.", e);
    }
}
