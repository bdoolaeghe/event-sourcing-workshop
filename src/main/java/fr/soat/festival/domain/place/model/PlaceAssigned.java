package fr.soat.festival.domain.place.model;

import fr.soat.festival.domain.spectator.model.Spectator;
import fr.soat.festival.domain.spectator.model.SpectatorId;
import lombok.*;

import static fr.soat.eventsourcing.api.Event.append;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class PlaceAssigned implements PlaceEvent {

    private SpectatorId assignee;

    @Override
    public Place applyOn(Place place) {
        return place.toBuilder()
                .assignee(assignee)
                .status(Place.Status.ASSIGNED)
                .events(append(place.getEvents(), this))
                .build();
    }

}
