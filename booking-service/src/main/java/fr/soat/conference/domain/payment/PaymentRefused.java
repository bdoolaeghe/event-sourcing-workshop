package fr.soat.conference.domain.payment;

import fr.soat.conference.domain.order.OrderId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
@Getter
public class PaymentRefused extends AccountEvent {

    private final int amount;
    private final OrderId orderId;

    public PaymentRefused(AccountId id, int amount, OrderId orderId) {
        super(id);
        this.amount = amount;
        this.orderId = orderId;
    }

    @Override
    public void applyOn(Account account) {
        account.apply(this);
    }

}
