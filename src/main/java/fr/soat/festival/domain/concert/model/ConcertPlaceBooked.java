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
public class ConcertPlaceBooked implements ConcertEvent {

    private PlaceId bookedPlaceId;

    @Override
    public Concert applyOn(Concert concert) {
        List<PlaceId> newAvailablePlaces = new ArrayList<>(concert.getAvailablePlaces());
        newAvailablePlaces.remove(bookedPlaceId);
        Concert.Status newStatus = (newAvailablePlaces.isEmpty())
                ? Concert.Status.FULL
                : Concert.Status.BOOKABLE;

        return concert.toBuilder()
                .availablePlaces(unmodifiableList(newAvailablePlaces))
                .status(newStatus)
                .events(append(concert.getEvents(), this))
                .build();
    }
}
