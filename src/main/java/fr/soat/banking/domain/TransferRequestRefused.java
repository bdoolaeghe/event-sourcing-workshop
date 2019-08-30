package fr.soat.banking.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
public class TransferRequestRefused extends TransferEvent {
    @Getter
    private final AccountId receiverAccountId;

    public TransferRequestRefused(AccountId accountId, AccountId receiverAccountId, Integer amount) {
        super(accountId,  amount);
        this.receiverAccountId = receiverAccountId;
    }

    @Override
    void applyOn(Account account) {
        account.apply(this);
    }
}
