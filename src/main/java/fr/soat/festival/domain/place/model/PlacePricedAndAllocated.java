package fr.soat.festival.domain.place.model;

import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.concert.model.Concert;
import fr.soat.festival.domain.concert.model.ConcertEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static fr.soat.eventsourcing.api.Event.concat;
import static java.util.Collections.unmodifiableList;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlacePricedAndAllocated implements PlaceEvent {

    private Artist artist;
    private int price;

    @Override
    public Place applyOn(Place place) {
        return place.toBuilder()
                .artist(artist)
                .price(price)
                .status(Place.Status.AVAILABLE)
                .events(concat(place.getEvents(), this))
                .build();
    }

}
