package fr.soat.conference.domain.order;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
public class OrderConfirmed extends OrderEvent {

    public OrderConfirmed(OrderId orderId) {
        super(orderId);
    }

    @Override
    public void applyOn(Order order) {
        order.apply(this);
    }

}
