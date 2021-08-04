package fr.soat.festival.domain.spectator.model;

import fr.soat.eventsourcing.api.DecisionFunction;
import fr.soat.eventsourcing.api.Entity;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.place.model.PlaceId;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

@Value
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Spectator implements Entity<SpectatorId, SpectatorEvent> {

    SpectatorId id;
    Map<Artist, Booking> bookings;

    List<SpectatorEvent> events;
    int version;

    public static Spectator create() {
        return create(null, 0);
    }

    public static Spectator create(SpectatorId id, int version) {
        return new Spectator(
                id,
                emptyMap(),
                emptyList(),
                version
        );
    }

    @DecisionFunction
    public Spectator rejectBooking(Artist artist) {
        return new SpectatorBookingRejected(artist).applyOn(this);
    }

    @DecisionFunction
    public Spectator registerBooking(PlaceId placeId, Artist artist) {
        return new SpectatorBookingRegistered(artist, placeId).applyOn(this);
    }

    @DecisionFunction
    public Spectator cancelBooking(Artist artist) {
        return new SpectatorBookingCanceled(artist).applyOn(this);
    }

    public Booking getBooking(Artist artist) {
        return bookings.get(artist);
    }

}
