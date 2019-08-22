package fr.soat.banking.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
public class TransferRequested extends TransferEvent {

    @Getter
    private final AccountId receiverAccountId;

    public TransferRequested(AccountId accountId, AccountId receiverAccountId, Integer amount) {
        super(accountId,  amount);
        this.receiverAccountId = receiverAccountId;
    }
    @Override
    public void applyOn(TransferProcessManager transferProcessManager) {
        transferProcessManager.on(this);
    }

    @Override
    void applyOn(Account account) {
        account.apply(this);
    }
}
