package fr.soat.festival.domain.concert.model;


import fr.soat.festival.domain.place.model.PlaceId;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static fr.soat.eventsourcing.api.Event.append;
import static java.util.Collections.unmodifiableList;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ConcertPlaceBookingCanceled implements ConcertEvent {

    private PlaceId placeId;

    @Override
    public Concert applyOn(Concert concert) {
        return concert.toBuilder()
                .status(Concert.Status.BOOKABLE)
                .availablePlaces(append(concert.getAvailablePlaces(), placeId))
                .events(append(concert.getEvents(), this))
                .build();
    }
}
