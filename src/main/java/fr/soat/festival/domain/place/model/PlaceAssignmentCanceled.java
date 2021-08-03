package fr.soat.festival.domain.place.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static fr.soat.util.Util.append;

@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PlaceAssignmentCanceled implements PlaceEvent {

    @Override
    public Place applyOn(Place place) {
        return place.toBuilder()
                .assignee(null)
                .status(Place.Status.AVAILABLE)
                .events(append(place.getEvents(), this))
                .build();
    }

}
