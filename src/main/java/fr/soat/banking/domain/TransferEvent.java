package fr.soat.banking.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
public abstract class TransferEvent extends AccountEvent {

    @Getter
    private final Integer amount;

    public TransferEvent(AccountId accountId, int amount) {
        super(accountId);
        this.amount = amount;
    }

}
