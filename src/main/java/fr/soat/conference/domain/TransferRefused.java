package fr.soat.conference.domain;

import fr.soat.conference.domain.order.AccountId;
import fr.soat.conference.domain.order.Order;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
public class TransferRefused extends TransferEvent {
    @Getter
    private final AccountId receiverAccountId;

    public TransferRefused(AccountId accountId, AccountId receiverAccountId, Integer amount) {
        super(accountId,  amount);
        this.receiverAccountId = receiverAccountId;
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
