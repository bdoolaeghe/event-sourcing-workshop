package fr.soat.festival.domain.place.model;

import fr.soat.eventsourcing.api.DecisionFunction;
import fr.soat.eventsourcing.api.Entity;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.spectator.model.SpectatorId;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Value
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Place implements Entity<PlaceId, PlaceEvent> {

    public enum Status {
        NEW,
        AVAILABLE,
        ASSIGNED
    }

    PlaceId id;
    Status status;
    Integer price;
    Artist artist;
    SpectatorId assignee;

    List<PlaceEvent> events;
    int version;

    public static Place create() {
        return create(null, 0);
    }

    public static Place create(PlaceId id, int version) {
        return new Place(
                id,
                Status.NEW,
                null,
                null,
                null,
                emptyList(),
                version
        );
    }

    @DecisionFunction
    public Place allocateTo(Artist artist, int price) {
        return new PlacePricedAndAllocated(artist, price).applyOn(this);
    }

    @DecisionFunction
    public Place assignTo(SpectatorId spectatorId) {
        return new PlaceAssigned(spectatorId).applyOn(this);
    }

    @DecisionFunction
    public Place cancelBooking() {
        return new PlaceAssignmentCanceled(artist, assignee, id).applyOn(this);
    }

}