package fr.soat.conference.domain.booking;

import fr.soat.conference.domain.order.OrderId;
import fr.soat.eventsourcing.api.DecisionFunction;
import fr.soat.eventsourcing.api.Entity;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.Optional;

import static fr.soat.conference.domain.booking.ConferenceStatus.FULL;
import static fr.soat.conference.domain.booking.ConferenceStatus.NEW;
import static java.util.Collections.unmodifiableList;

@Value
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Conference implements Entity<ConferenceName, ConferenceEvent> {

    ConferenceName conferenceName;
    ConferenceStatus status;
    List<ConferenceEvent> events;
    List<Seat> seats;
    List<Seat> availableSeats;
    Integer seatPrice;

    public static Conference create(ConferenceName conferenceName) {
        return Conference.builder()
                .conferenceName(conferenceName)
                .status(NEW)
                .seats(unmodifiableList())
                .availableSeats(unmodifiableList())
                .events(unmodifiableList())
                .build();
    }

    @DecisionFunction
    public Conference open(int places, int seatPrice) {
        return new ConferenceOpened(getId(), places, seatPrice).applyOn(this);
    }

    @DecisionFunction
    public Optional<Seat> bookSeat(OrderId orderId) {
        if (status == FULL) {
            new SeatBookingRequestRefused(getId(), orderId).applyOn(this);
            return Optional.empty();
        } else {
            Seat bookedSeat = availableSeats.get(0);
            new SeatBooked(getId(), orderId, bookedSeat).applyOn(this);
            return Optional.of(bookedSeat);
        }
    }

    @DecisionFunction
    public void cancelBooking(Seat seat) {
        new SeatReleased(getId(), seat).applyOn(this);
    }

    @Override
    public ConferenceName getId() {
        return getConferenceName();
    }

    @Override
    public String toString() {
        return "room " + this.getId().getName() +
               ": " +
               availableSeats.size() + " / " + seats.size() + " available seats" +
               " (" + status + ")";
    }

}
