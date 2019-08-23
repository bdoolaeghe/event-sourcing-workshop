package fr.soat.conference.infra.order;

import fr.soat.conference.domain.order.Order;
import fr.soat.conference.domain.order.AccountEvent;
import fr.soat.conference.domain.order.AccountId;
import fr.soat.eventsourcing.api.Event;
import fr.soat.eventsourcing.api.EventStore;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
public class OrderRepository {

    private final EventStore eventStore;

    public OrderRepository(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public void save(Order order) {
        AccountId aggregateId = order.getId();
        eventStore.store(aggregateId, order.getChanges());
    }

    public Order load(AccountId accountId) {
        List<AccountEvent> events = asAccountEvents(eventStore.loadEvents(accountId));
        return hydrate(accountId, events);
    }

    private static Order hydrate(AccountId accountId, List<AccountEvent> events) {
        Order order = new Order(accountId);
        events.forEach(event -> event.applyOn(order));
        return order;
    }

    private List<AccountEvent> asAccountEvents(List<Event> events) {
        return events.stream().map(event -> (AccountEvent) event).collect(toList());
    }
}
