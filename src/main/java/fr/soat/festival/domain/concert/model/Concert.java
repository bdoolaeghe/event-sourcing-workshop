package fr.soat.festival.domain.concert.model;

import fr.soat.eventsourcing.api.DecisionFunction;
import fr.soat.eventsourcing.api.Entity;
import fr.soat.festival.domain.place.model.PlaceId;
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
    public Concert assignRoom(List<PlaceId> availablePlaces) {
        return new ConcertRoomAssigned(availablePlaces)
                .applyOn(this);
    }

    public Concert book(PlaceId placeId) {
        if (availablePlaces.contains(placeId)) {
            return new ConcertPlaceBooked(placeId)
                    .applyOn(this);
        } else {
            throw new IllegalArgumentException("Can not book place " + placeId +
                                               " because this place is not available");
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
