package fr.soat.conference.domain.order;

import fr.soat.conference.domain.booking.ConferenceName;
import fr.soat.conference.domain.payment.AccountId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
@Getter
public class OrderCreated extends OrderEvent {

    private final ConferenceName conferenceName;
    private final AccountId accountId;

    public OrderCreated(OrderId id, ConferenceName conferenceName, AccountId accountForPayment) {
        super(id);
        this.conferenceName = conferenceName;
        this.accountId = accountForPayment;
    }

    @Override
    public void applyOn(Order order) {
        order.apply(this);
    }

}
