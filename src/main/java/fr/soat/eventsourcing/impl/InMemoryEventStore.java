package fr.soat.eventsourcing.impl;

import com.google.common.collect.ArrayListMultimap;
import fr.soat.eventsourcing.api.*;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.reverse;

public class InMemoryEventStore implements EventStore {

    private final ArrayListMultimap<String, Event> store = ArrayListMultimap.create();
    private final Object lock = new Object();
    @Getter
    private final EventBus eventBus;

    public InMemoryEventStore(EventBus eventBus) {
        this.eventBus = eventBus;
    }

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
                Event evnet = events.get(i);
                previousEvents.add(evnet);
                eventBus.publish(evnet);
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
}
