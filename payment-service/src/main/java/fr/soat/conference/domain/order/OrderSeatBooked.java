package fr.soat.conference.domain.order;

import fr.soat.conference.domain.booking.Seat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
@Getter
public class OrderSeatBooked extends OrderEvent {

    private final Seat bookedSeat;

    public OrderSeatBooked(OrderId orderId, Seat bookedSeat) {
        super(orderId);
        this.bookedSeat = bookedSeat;
    }

    @Override
    public void applyOn(Order order) {
        order.apply(this);
    }
}
