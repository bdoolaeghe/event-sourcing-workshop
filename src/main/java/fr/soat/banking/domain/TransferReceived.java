package fr.soat.banking.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
public class TransferReceived extends AccountEvent {

    @Getter
    private final Integer amount;
    @Getter
    private final AccountId targetAccountId;

    public TransferReceived(AccountId fromAccountId, AccountId toAccountId, Integer amount) {
        super(fromAccountId);
        this.targetAccountId = toAccountId;
        this.amount = amount;
    }

    @Override
    void applyOn(Account account) {
        account.apply(this);
    }

}
