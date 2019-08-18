package fr.soat.banking.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@EqualsAndHashCode
@ToString(callSuper = true)
public class AccountDeposited extends AccountEvent {

    @Getter
    private Amount depositedAmount;

    public AccountDeposited(AccountId accountId, Amount depositedAmount) {
        super(accountId);
        this.depositedAmount = depositedAmount;
    }

    @Override
    public void applyOn(Account account) {
        account.apply(this);
    }
}
