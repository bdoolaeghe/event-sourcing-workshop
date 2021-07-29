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

    public static Concert create(Artist artist) {
        return Concert.builder()
                .artist(artist)
                .status(Status.NEW)
                .availablePlaces(emptyList())
                .events(emptyList())
                .build();
    }

    @DecisionFunction
    public Concert assignRoom(List<PlaceId> availablePlaces) {
        return new ConcertRoomAssigned(availablePlaces)
                .applyOn(this);
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
