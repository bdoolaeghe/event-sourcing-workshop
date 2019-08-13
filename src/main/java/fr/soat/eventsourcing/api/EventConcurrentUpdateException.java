package fr.soat.eventsourcing.api;

public class EventConcurrentUpdateException extends RuntimeException {
    public EventConcurrentUpdateException(String msg) {
        super(msg);
    }
}
