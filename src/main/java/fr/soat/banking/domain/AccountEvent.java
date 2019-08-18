package fr.soat.banking.domain;

import fr.soat.eventsourcing.api.AggregateId;
import fr.soat.eventsourcing.api.Event;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString(of = "accountId")
public abstract class AccountEvent implements Event {

    private AccountId accountId;

    protected AccountEvent() {}

    public AggregateId getAggregateId() {
        return accountId;
    }

    abstract void applyOn(Account account);
}
