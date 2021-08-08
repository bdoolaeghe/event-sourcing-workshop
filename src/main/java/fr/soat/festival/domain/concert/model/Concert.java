package fr.soat.festival.domain.concert.model;

import fr.soat.eventsourcing.api.DecisionFunction;
import fr.soat.eventsourcing.api.Entity;
import fr.soat.festival.domain.place.model.PlaceId;
import fr.soat.festival.domain.spectator.model.SpectatorId;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Value
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Concert implements Entity<Artist, ConcertEvent> {

    public enum Status {
        NEW,
        BOOKABLE,
        FULL
    }

    Artist artist;
    Status status;
    List<PlaceId> availablePlaces;
    int roomSize;

    List<ConcertEvent> events;
    int version;

    public static Concert create(Artist artist) {
        return create(artist, 0);
    }

    public static Concert create(Artist artist, int version) {
        return Concert.builder()
                .artist(artist)
                .status(Status.NEW)
                .availablePlaces(emptyList())
                .events(emptyList())
                .version(version)
                .build();
    }

    @DecisionFunction
    public Concert open(int places, int price) {
        return new ConcertOpeningRequested(artist, places, price).applyOn(this);
    }

    @DecisionFunction
    public Concert assignRoom(List<PlaceId> availablePlaces) {
        return new ConcertOpened(availablePlaces)
                .applyOn(this);
    }

    @DecisionFunction
    public Concert requestBooking(Artist artist, SpectatorId spectatorId) {
        if (this.isFull()) {
            return new ConcertPlaceBookingRequestRejected(artist, spectatorId).applyOn(this);
        } else {
            PlaceId placeId = getAnAvailablePlaceId();
            return new ConcertPlaceBooked(artist, spectatorId, placeId).applyOn(this);
        }
    }

    @DecisionFunction
    public Concert cancelBooking(PlaceId placeId) {
        if (availablePlaces.contains(placeId)) {
            throw new IllegalArgumentException("Can not cancel booking of place " + placeId +". Place already available for concert " + artist);
        } else {
            return new ConcertPlaceBookingCanceled(placeId)
                    .applyOn(this);
        }
    }

    public boolean isFull() {
        return status == Status.FULL;
    }

    public PlaceId getAnAvailablePlaceId() {
       return availablePlaces.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Concert is " +
                                                                status + ". Can not provide any available place !"));
    }

    @Override
    public Artist getId() {
        return getArtist();
    }

}
