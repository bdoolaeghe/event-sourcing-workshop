package fr.soat.conference.domain.payment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
@Getter
public class AccountCredited extends AccountEvent {

    private final int amount;

    public AccountCredited(AccountId accountId, int amount) {
        super(accountId);
        this.amount = amount;
    }

    @Override
    public void applyOn(Account account) {
        account.apply(this);
    }
}
