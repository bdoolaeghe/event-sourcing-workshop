package fr.soat.festival.domain.concert.model;


import fr.soat.festival.domain.spectator.model.SpectatorId;
import lombok.*;

import static fr.soat.util.Util.append;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ConcertPlaceBookingRequestRejected implements ConcertEvent {

    private Artist artist;
    private SpectatorId spectatorId;

    @Override
    public Concert applyOn(Concert concert) {
        return concert.toBuilder()
                .events(append(concert.getEvents(), this))
                .build();
    }
}
