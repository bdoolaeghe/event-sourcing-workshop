package fr.soat.banking.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
public class TransferReceived extends TransferEvent {

    @Getter
    private final AccountId senderAccountId;

    public TransferReceived(AccountId accountId, AccountId senderAccountId, Integer amount) {
        super(accountId,  amount);
        this.senderAccountId = senderAccountId;
    }

    @Override
    public void applyOn(TransferProcessManager transferProcessManager) {
        transferProcessManager.on(this);
    }

    @Override
    void applyOn(Account account) {
        account.on(this);
    }
}
