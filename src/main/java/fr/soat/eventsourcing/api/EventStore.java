package fr.soat.eventsourcing.api;

import java.util.List;

public interface EventStore {

    List<Event> loadEvents(AggregateId aggregateId);
    void store(AggregateId aggregateId, List<Event> events);
}
