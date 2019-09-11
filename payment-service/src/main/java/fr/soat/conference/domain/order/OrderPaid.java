package fr.soat.conference.domain.order;

import fr.soat.conference.domain.payment.PaymentReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
@Getter
public class OrderPaid extends OrderEvent {

    private final PaymentReference paymentReference;

    public OrderPaid(OrderId orderId, PaymentReference paymentReference) {
        super(orderId);
        this.paymentReference  = paymentReference;
    }

    @Override
    public void applyOn(Order order) {
        order.apply(this);
    }
}
