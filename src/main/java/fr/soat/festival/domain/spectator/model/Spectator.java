package fr.soat.festival.domain.spectator.model;

import fr.soat.festival.domain.place.model.PlaceId;
import fr.soat.eventsourcing.api.Entity;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Spectator implements Entity<SpectatorId, SpectatorEvent> {

    SpectatorId id;
    List<PlaceId> bookings;

    List<SpectatorEvent> events;

    public List<SpectatorEvent> getEvents() {
        return events;
    }
}
