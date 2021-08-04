package fr.soat.festival.domain.place.model;

import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.spectator.model.SpectatorId;
import lombok.*;

import static fr.soat.util.Util.append;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class PlaceAssignmentCanceled implements PlaceEvent {

    private Artist artist;
    private SpectatorId spectatorId;
    private PlaceId placeId;

    @Override
    public Place applyOn(Place place) {
        return place.toBuilder()
                .assignee(null)
                .status(Place.Status.AVAILABLE)
                .events(append(place.getEvents(), this))
                .build();
    }

}
