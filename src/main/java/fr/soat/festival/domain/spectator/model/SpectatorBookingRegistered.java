package fr.soat.festival.domain.spectator.model;

import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.place.model.PlaceId;
import lombok.*;

import static fr.soat.eventsourcing.api.Event.append;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SpectatorBookingRegistered implements SpectatorEvent {

    private PlaceId placeId;

    @Override
    public Spectator applyOn(Spectator spectator) {
        return spectator.toBuilder()
                .bookings(append(spectator.getBookings(), placeId))
                .events(append(spectator.getEvents(), this))
                .build();
    }

}
