package fr.soat.festival.domain.concert.model;


import fr.soat.festival.domain.place.model.PlaceId;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ConcertPlaceBookingCancelled implements ConcertEvent {

    private PlaceId releasedPlace;

    @Override
    public Concert applyOn(Concert concert) {
        throw new IllegalStateException("implemented me !");
    }
}
