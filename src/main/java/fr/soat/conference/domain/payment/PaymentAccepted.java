package fr.soat.conference.domain.payment;

import fr.soat.conference.domain.order.OrderId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
@Getter
public class PaymentAccepted extends AccountEvent {

    private final int amount;
    private final OrderId orderId;
    private final PaymentReference paymentReference;

    public PaymentAccepted(PaymentReference paymentReference, AccountId id, int amount, OrderId orderId) {
        super(id);
        this.amount = amount;
        this.orderId = orderId;
        this.paymentReference = paymentReference;
    }

    @Override
    public void applyOn(Account account) {
        account.apply(this);
    }

}
