package fr.soat.festival.domain.place.model;

import fr.soat.festival.domain.concert.model.Artist;
import lombok.*;

import static fr.soat.eventsourcing.api.Event.concat;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
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
