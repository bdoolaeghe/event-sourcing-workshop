package fr.soat.eventsourcing.api;

public interface EventListener {

    void on(Event event);

}
