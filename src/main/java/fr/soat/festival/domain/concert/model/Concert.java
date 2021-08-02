package fr.soat.festival.domain.concert.model;

import fr.soat.festival.domain.place.model.Place;
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

    @DecisionFunction
    public Concert book(PlaceId placeId) {
        if (availablePlaces.contains(placeId)) {
            return new ConcertPlaceBooked(placeId)
                    .applyOn(this);
        } else {
            throw new IllegalArgumentException("Can not book place " + placeId +
                                               " because this place is not available");
        }
    }


//    @DecisionFunction
//    public Optional<Place> bookSeat(OrderId orderId) {
//        if (status == FULL) {
//            new SeatBookingRequestRefused(getId(), orderId).applyOn(this);
//            return Optional.empty();
//        } else {
//            Place bookedPlace = availableSeats.get(0);
//            new SeatBooked(getId(), orderId, bookedPlace).applyOn(this);
//            return Optional.of(bookedPlace);
//        }
//    }
//
//    @DecisionFunction
//    public void cancelBooking(Place place) {
//        new SeatReleased(getId(), place).applyOn(this);
//    }

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

//    @Override
//    public String toString() {
//        return "room " + this.getId().getName() +
//               ": " +
//               availableSeats.size() + " / " + places.size() + " available seats" +
//               " (" + status + ")";
//    }

}
