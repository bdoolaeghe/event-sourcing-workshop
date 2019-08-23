package fr.soat.conference.domain;

import fr.soat.conference.domain.order.AccountEvent;
import fr.soat.conference.domain.order.AccountId;
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

    public abstract void applyOn(ReservationProcessManager reservationProcessManager);
}
