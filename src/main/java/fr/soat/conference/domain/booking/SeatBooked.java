package fr.soat.conference.domain.booking;

import fr.soat.conference.domain.order.OrderId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
@Getter
public class SeatBooked extends ConferenceEvent {

    private final OrderId orderId;
    private final Seat seat;

    public SeatBooked(ConferenceName conferenceName, OrderId orderId, Seat seat) {
        super(conferenceName);
        this.orderId = orderId;
        this.seat = seat;
    }

    @Override
    public void applyOn(Conference conference) {
        conference.apply(this);
    }
}
