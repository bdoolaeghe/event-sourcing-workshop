package fr.soat.festival.domain.spectator.model;

import fr.soat.eventsourcing.api.Entity;
import fr.soat.festival.domain.place.model.PlaceId;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Value
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Spectator implements Entity<SpectatorId, SpectatorEvent> {

    SpectatorId id;
    List<PlaceId> bookings;

    List<SpectatorEvent> events;

    public static Spectator create() {
        return create(null);
    }

    public static Spectator create(SpectatorId id) {
        return new Spectator(
                id,
                emptyList(),
                emptyList()
        );
    }

    public List<SpectatorEvent> getEvents() {
        return events;
    }
}
