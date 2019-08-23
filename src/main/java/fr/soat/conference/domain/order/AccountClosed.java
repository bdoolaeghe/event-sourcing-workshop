package fr.soat.conference.domain.order;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
public class AccountClosed extends AccountEvent {

    public AccountClosed(AccountId accountId) {
        super(accountId);
    }

    @Override
    public void applyOn(Order order) {
        order.apply(this);
    }

}
