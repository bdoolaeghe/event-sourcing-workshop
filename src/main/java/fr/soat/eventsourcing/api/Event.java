package fr.soat.eventsourcing.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public interface Event<ENTITY> {
    ENTITY applyOn(ENTITY entity);

    static <T extends Event> List<T> concat(List<T> events, T event) {
        if (events == null) {
            events = new ArrayList<>();
        }
        return Stream.concat(events.stream(),
                Stream.of(event))
                .collect(toList());
    }
}
