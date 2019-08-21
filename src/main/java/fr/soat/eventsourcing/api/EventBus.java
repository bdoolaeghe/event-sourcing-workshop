package fr.soat.eventsourcing.api;

public interface EventBus {
    void register(EventListener eventListener);
    void publish(Event event);
    void clear();
}
