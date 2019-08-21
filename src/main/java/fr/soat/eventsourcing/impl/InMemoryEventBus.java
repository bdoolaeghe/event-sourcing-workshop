package fr.soat.eventsourcing.impl;

import fr.soat.eventsourcing.api.Event;
import fr.soat.eventsourcing.api.EventBus;
import fr.soat.eventsourcing.api.EventListener;

import java.util.HashSet;
import java.util.Set;

public class InMemoryEventBus implements EventBus {

    private final Set<EventListener> listeners = new HashSet<>();

    @Override
    public void register(EventListener eventListener) {
        listeners.add(eventListener);
    }

    @Override
    public void publish(Event event) {
        for (EventListener listener : listeners) {
            listener.on(event);
        }
    }

    @Override
    public void clear() {
        listeners.clear();
    }
}
