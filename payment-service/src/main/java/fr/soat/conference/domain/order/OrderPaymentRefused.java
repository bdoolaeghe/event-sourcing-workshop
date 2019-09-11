package fr.soat.conference.domain.order;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
@Getter
public class OrderPaymentRefused extends OrderEvent {

    public OrderPaymentRefused(OrderId orderId) {
        super(orderId);
    }

    @Override
    public void applyOn(Order order) {
        order.apply(this);
    }
}
