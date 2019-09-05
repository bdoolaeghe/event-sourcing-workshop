package fr.soat.conference.domain.booking;

import fr.soat.conference.domain.order.OrderId;
import fr.soat.eventsourcing.api.AggregateRoot;
import fr.soat.eventsourcing.api.DecisionFunction;
import fr.soat.eventsourcing.api.EvolutionFunction;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fr.soat.conference.domain.booking.ConferenceStatus.*;

@Getter
public class Conference extends AggregateRoot<ConferenceName>  {

    private final List<Seat> seats = new ArrayList<>();
    private final List<Seat> availableSeats = new ArrayList<>();
    private int seatPrice;
    private ConferenceStatus status;

    public Conference(ConferenceName conferenceName) {
        super(conferenceName);
        this.status = NEW;
    }

    @DecisionFunction
    public Conference open(int places, int seatPrice) {
        apply(new ConferenceOpened(getId(), places, seatPrice));
        return this;
    }

    @EvolutionFunction
    public void apply(ConferenceOpened conferenceOpened) {
        this.status = OPEN;
        this.seats.clear();
        for (int i = 1; i < conferenceOpened.getPlaces() + 1; i++) {
            this.seats.add(new Seat(i));
        }
        this.availableSeats.clear();
        this.availableSeats.addAll(seats);
        this.seatPrice = conferenceOpened.getSeatPrice();
        recordChange(conferenceOpened);
    }

    @DecisionFunction
    public Optional<Seat> bookSeat(OrderId orderId) {
        if (status == FULL) {
            apply(new SeatBookingRequestRefused(getId(), orderId));
            return Optional.empty();
        } else {
            Seat bookedSeat = availableSeats.get(0);
            apply(new SeatBooked(getId(), orderId, bookedSeat));
            return Optional.of(bookedSeat);
        }
    }

    @DecisionFunction
    public void cancelBooking(Seat seat) {
        apply(new SeatReleased(getId(), seat));
    }

    @EvolutionFunction
    public void apply(SeatBooked conferenceSeatBooked) {
        this.availableSeats.remove(conferenceSeatBooked.getSeat());
        if (availableSeats.isEmpty()) {
            status = FULL;
        }
        recordChange(conferenceSeatBooked);
    }

    @EvolutionFunction
    public void apply(SeatBookingRequestRefused seatBookingRequestRefused) {
        recordChange(seatBookingRequestRefused);
    }

    @EvolutionFunction
    public void apply(SeatReleased seatReleased) {
        this.status = OPEN;
        this.availableSeats.add(seatReleased.getSeat());
        recordChange(seatReleased);
    }

    @Override
    public String toString() {
        return "room " + this.getId().getName() +
                ": " +
                availableSeats.size() + " / " + seats.size() + " available seats" +
                " (" + status + ")";
    }

}
