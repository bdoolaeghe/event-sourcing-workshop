package fr.soat.conference.domain.booking;

import com.google.common.collect.Lists;
import fr.soat.conference.domain.order.OrderId;
import fr.soat.eventsourcing.api.EvolutionFunction;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

import static fr.soat.eventsourcing.api.Event.concat;

@Value
@EqualsAndHashCode(callSuper = true)
public class SeatBooked extends ConferenceEvent {

    OrderId orderId;
    Seat seat;

    public SeatBooked(ConferenceName conferenceName, OrderId orderId, Seat seat) {
        super(conferenceName);
        this.orderId = orderId;
        this.seat = seat;
    }

    @Override
    @EvolutionFunction
    public Conference applyOn(Conference conference) {
        List<Seat> newAvailablerSeats = Lists.newArrayList(conference.getAvailableSeats());
        newAvailablerSeats.remove(seat);

        return conference.toBuilder()
                .availableSeats(newAvailablerSeats)
                .status((newAvailablerSeats.isEmpty() ? ConferenceStatus.FULL : conference.getStatus() ))
                .events(concat(conference.getEvents(), this))
                .build();
    }
}
