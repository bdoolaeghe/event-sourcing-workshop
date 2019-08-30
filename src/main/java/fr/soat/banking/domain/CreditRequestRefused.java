package fr.soat.banking.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
public class CreditRequestRefused extends TransferEvent {

    @Getter
    private final AccountId sourceAccountId;

    public CreditRequestRefused(AccountId id, AccountId sourceAccountId, int amount) {
        super(id, amount);
        this.sourceAccountId = sourceAccountId;
    }

    @Override
    void applyOn(Account account) {
        account.apply(this);
    }
}
