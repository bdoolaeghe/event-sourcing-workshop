package fr.soat.conference.domain.order;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
public class OrderSeatBookingFailed extends OrderEvent {

    public OrderSeatBookingFailed(OrderId orderId) {
        super(orderId);
    }

    @Override
    public void applyOn(Order order) {
        order.apply(this);
    }

}
