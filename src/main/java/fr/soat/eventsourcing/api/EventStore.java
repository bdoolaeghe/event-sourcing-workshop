package fr.soat.eventsourcing.api;

import com.google.common.annotations.VisibleForTesting;

import java.util.List;

public interface EventStore {

    List<Event> loadEvents(AggregateId aggregateId);
    void store(AggregateId aggregateId, List<Event> events);
    @VisibleForTesting
    void clear();
    String nextId();
}
