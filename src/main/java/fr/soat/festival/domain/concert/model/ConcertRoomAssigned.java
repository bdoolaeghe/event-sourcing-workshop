package fr.soat.festival.domain.concert.model;

import fr.soat.festival.domain.place.model.Place;
import fr.soat.festival.domain.place.model.PlaceId;
import lombok.*;

import java.util.List;

import static fr.soat.eventsourcing.api.Event.concat;
import static java.util.Collections.unmodifiableList;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ConcertRoomAssigned implements ConcertEvent {

    private List<PlaceId> availablePlaces;

    @Override
    public Concert applyOn(Concert concert) {
        return concert.toBuilder()
                .availablePlaces(unmodifiableList(availablePlaces))
                .status(Concert.Status.BOOKABLE)
                .events(concat(concert.getEvents(), this))
                .build();
    }

}
