package fr.soat.eventsourcing.api;

import java.util.concurrent.atomic.AtomicInteger;

public interface AggregateId {
    AtomicInteger idGenerator = new AtomicInteger(0);
    String getValue();
}
