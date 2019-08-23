package fr.soat.conference.domain;

import fr.soat.conference.domain.order.AccountId;
import fr.soat.conference.domain.order.Order;
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
    public void applyOn(ReservationProcessManager reservationProcessManager) {
        reservationProcessManager.on(this);
    }

    @Override
    public void applyOn(Order order) {
        order.apply(this);
    }
}
