package fr.soat.banking.domain;

import fr.soat.eventsourcing.api.AggregateId;
import fr.soat.eventsourcing.api.Event;
import fr.soat.eventsourcing.api.EventListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString(of = "accountId")
public abstract class AccountEvent implements Event {

    private final AccountId accountId;

    public AggregateId getAggregateId() {
        return accountId;
    }

    abstract void applyOn(Account account);

    @Override
    public void applyOn(EventListener eventListener) {}

}
