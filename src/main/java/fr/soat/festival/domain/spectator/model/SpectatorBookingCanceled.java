package fr.soat.festival.domain.spectator.model;

import fr.soat.festival.domain.concert.model.Artist;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

import static fr.soat.util.Util.append;
import static java.util.Collections.unmodifiableMap;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SpectatorBookingCanceled implements SpectatorEvent {

    private Artist artist;

    @Override
    public Spectator applyOn(Spectator spectator) {
        Map<Artist, Booking> bookings = new HashMap<>(spectator.getBookings());
        bookings.remove(artist);
        return spectator.toBuilder()
                .bookings(unmodifiableMap(bookings))
                .events(append(spectator.getEvents(), this))
                .build();
    }

}
