package fr.soat.eventsourcing.api;

public interface Event {
    AggregateId getAggregateId();
}
