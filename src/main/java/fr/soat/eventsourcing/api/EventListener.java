package fr.soat.eventsourcing.api;

public interface EventListener<T extends Event> {

    void on(T event);

}
