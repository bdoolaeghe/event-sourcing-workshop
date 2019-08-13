package fr.soat.banking.domain;

import fr.soat.eventsourcing.api.Event;
import fr.soat.eventsourcing.api.EventStore;

import java.util.List;
import java.util.stream.Collectors;


public class AccountRepository {

    private final EventStore eventStore;

    public AccountRepository(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    void save(Account account) {
        AccountId aggregateId = account.getId();
        eventStore.store(aggregateId, account.getChanges());
    }

    Account load(AccountId accountId) {
        List<AccountEvent> events = asAccountEvents(eventStore.loadEvents(accountId));
        return Account.hydrate(accountId, events);
    }

    private List<AccountEvent> asAccountEvents(List<Event> events) {
        return events.stream().map(event -> (AccountEvent) event).collect(Collectors.toList());
    }
}
