package fr.soat.eventsourcing.impl;

import com.google.common.collect.ArrayListMultimap;
import fr.soat.eventsourcing.api.AggregateId;
import fr.soat.eventsourcing.api.Event;
import fr.soat.eventsourcing.api.EventConcurrentUpdateException;
import fr.soat.eventsourcing.api.EventStore;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.Lists.reverse;

public class InMemoryEventStore implements EventStore {

    private final ArrayListMultimap<String, Event> store = ArrayListMultimap.create();
    private final Object lock = new Object();
    private AtomicInteger idGenerator = new AtomicInteger(1);


    @Override
    public List<Event> loadEvents(AggregateId aggregateId) {
        synchronized (lock) {
            return store.get(aggregateId.toString());
        }
    }

    @Override
    public void store(AggregateId aggregateId, List<Event> events) {
        synchronized (lock) {
            List<Event> previousEvents = loadEvents(aggregateId);
            checkVersion(previousEvents, events);

            for (int i = previousEvents.size(); i < events.size(); i++) {
                previousEvents.add(events.get(i));
            }
        }
    }

    private void checkVersion(List<Event> previousEvents, List<Event> events) {
        if (Collections.indexOfSubList(events, previousEvents) != 0) {
            String msg = "Failed to save events, version mismatch (there was a concurrent update)\n" +
                    "- In store: " + reverse(previousEvents) + "\n" +
                    "- Trying to save: " + reverse(events);
            throw new EventConcurrentUpdateException(msg);
        }
    }

    @Override
    public void clear() {
        store.clear();
    }

    @Override
    public String nextId() {
        return String.valueOf(idGenerator.getAndIncrement());
    }
}
