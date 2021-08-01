package fr.soat.festival.domain.spectator.model;

import fr.soat.festival.domain.concert.model.Artist;
import lombok.*;

import static fr.soat.eventsourcing.api.Event.append;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SpectatorBookingRejected implements SpectatorEvent {

    private Artist artist;

    @Override
    public Spectator applyOn(Spectator spectator) {
        return spectator.toBuilder()
                .rejectedBookings(append(spectator.getRejectedBookings(), artist))
                .events(append(spectator.getEvents(), this))
                .build();
    }

}
