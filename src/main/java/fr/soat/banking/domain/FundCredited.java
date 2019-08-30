package fr.soat.banking.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
public class FundCredited extends TransferEvent {

    @Getter
    private final AccountId senderAccountId;

    public FundCredited(AccountId accountId, AccountId senderAccountId, Integer amount) {
        super(accountId,  amount);
        this.senderAccountId = senderAccountId;
    }

    @Override
    void applyOn(Account account) {
        account.apply(this);
    }
}
