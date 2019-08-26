package fr.soat.conference.domain.payment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
@Getter
public class PaymentRequested extends AccountEvent {

    private final int amount;

    public PaymentRequested(AccountId id, int amount) {
        super(id);
        this.amount = amount;
    }

    @Override
    public void applyOn(Account account) {
        account.apply(this);
    }

}
