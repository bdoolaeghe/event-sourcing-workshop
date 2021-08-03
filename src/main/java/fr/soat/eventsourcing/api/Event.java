package fr.soat.eventsourcing.api;

public interface Event<ENTITY> {
    ENTITY applyOn(ENTITY entity);
}
