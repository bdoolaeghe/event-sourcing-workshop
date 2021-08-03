package fr.soat.festival.domain.spectator.model;

import fr.soat.festival.domain.place.model.PlaceId;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;

import static fr.soat.eventsourcing.api.Event.append;
import static java.util.Collections.unmodifiableList;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SpectatorBookingCanceled implements SpectatorEvent {

    private PlaceId placeId;

    @Override
    public Spectator applyOn(Spectator spectator) {
        ArrayList<PlaceId> newBookings = new ArrayList<>(spectator.getBookings());
         newBookings.remove(placeId);
        return spectator.toBuilder()
                .bookings(unmodifiableList(newBookings))
                .events(append(spectator.getEvents(), this))
                .build();
    }

}
