package fr.soat.banking.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@EqualsAndHashCode
@ToString(callSuper = true)
public class AccountWithdrawn extends AccountEvent {

    @Getter
    private Amount withdrawnAmount;

    public AccountWithdrawn(AccountId accountId, Amount withdrawnAmount) {
        super(accountId);
        this.withdrawnAmount = withdrawnAmount;
    }

    @Override
    public void applyOn(Account account) {
        account.apply(this);
    }
}
