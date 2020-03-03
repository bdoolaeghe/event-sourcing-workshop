package fr.soat.eventsourcing.impl.db;

import fr.soat.conference.application.configuration.ConferenceConfiguration;
import fr.soat.conference.domain.booking.Conference;
import fr.soat.conference.domain.booking.ConferenceName;
import fr.soat.conference.domain.booking.Seat;
import fr.soat.conference.domain.order.OrderId;
import fr.soat.eventsourcing.api.Event;
import fr.soat.eventsourcing.configuration.DbEventStoreConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static fr.soat.conference.domain.booking.ConferenceName.name;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        DbEventStoreConfiguration.class,
        ConferenceConfiguration.class
})
public class DBEventStoreTest {

//    @Test
//    public void should_save_and_reload_any_kind_of_event() {
//        throw new RuntimeException("implement me !");
//    }

    @Autowired
    DBEventStore dbEventStore;
    private OrderId orderId = OrderId.next();

    @Before
    public void setUp() throws Exception {
        dbEventStore.clear();
    }

    @Test
    public void should_store_and_reload_conference_events() {
        // Given
        orderId = OrderId.next();
        ConferenceName conferenceName = name("any conference name");
        Conference conference = new Conference(conferenceName);
        Seat seat = conference
                .open(100, 10)
                .bookSeat(orderId).get();
        conference.cancelBooking(seat);
        List<Event> events = conference.getChanges();

        // When
        dbEventStore.store(orderId, events);
        List<Event> reloadedEvents = dbEventStore.loadEvents(orderId);

        //FIXME test the entity rather than events
        // Then
        assertThat(reloadedEvents).isEqualTo(events);
    }

    @Test
    public void should_store_and_reload_empty_event_list() {
        // Given
        orderId = OrderId.next();
        List<Event> events = Collections.emptyList();

        // When
        dbEventStore.store(orderId, events);
        List<Event> reloadedEvents = dbEventStore.loadEvents(orderId);

        // Then
        assertThat(reloadedEvents).isEqualTo(events);
    }

//    @Test
//    public void should_store_be_idempotent() {
//        // Given
//        orderId = OrderId.next();
//        List<Event> events = asList(
//                new OrderCreated(orderId, "toto", "1234-5678-9101"),
//                new AccountDeposited(orderId, 100),
//                new AccountWithdrawn(orderId, 50),
//                new OrderConfirmed(orderId)
//        );
//        dbEventStore.store(orderId, events);
//
//        // When
//        List<Event> sameEvents = asList(
//                new OrderCreated(orderId, "toto", "1234-5678-9101"),
//                new AccountDeposited(orderId, 100),
//                new AccountWithdrawn(orderId, 50),
//                new OrderConfirmed(orderId)
//        );
//        dbEventStore.store(orderId, sameEvents);
//        List<Event> reloadedEvents = dbEventStore.loadEvents(orderId);
//
//        // Then
//        assertThat(reloadedEvents).isEqualTo(events);
//        assertThat(reloadedEvents).isEqualTo(sameEvents);
//    }
//
//    @Test
//    public void should_store_a_new_event_on_existing_events() {
//
//        // Given
//        orderId = OrderId.next();
//        List<Event> events = newArrayList(
//                new OrderCreated(orderId, "toto", "1234-5678-9101"),
//                new AccountDeposited(orderId, 200)
//        );
//        dbEventStore.store(orderId, events);
//
//        // When
//        events.add(new OrderConfirmed(orderId));
//        dbEventStore.store(orderId, events);
//
//        // Then
//        List<Event> reloadedEvents = dbEventStore.loadEvents(orderId);
//        assertThat(reloadedEvents).isEqualTo(events);
//    }
//
//    @Test
//    public void should_store_new_events_on_existing_events() {
//
//        // Given
//        orderId = OrderId.next();
//        List<Event> events = newArrayList(
//                new OrderCreated(orderId, "toto", "1234-5678-9101"),
//                new AccountDeposited(orderId, 200)
//        );
//        dbEventStore.store(orderId, events);
//
//        // When
//        events.add(new AccountDeposited(orderId, 10));
//        events.add(new AccountDeposited(orderId, 20));
//        events.add(new AccountDeposited(orderId, 30));
//        dbEventStore.store(orderId, events);
//
//        // Then
//        List<Event> reloadedEvents = dbEventStore.loadEvents(orderId);
//        assertThat(reloadedEvents).isEqualTo(events);
//    }
//
//    @Test
//    public void should_fail_to_store_when_one_concurrent_modification_occurs() {
//        // Given
//        orderId = OrderId.next();
//        List<Event> events = asList(
//                new OrderCreated(orderId, "toto", "1234-5678-9101"),
//                new AccountDeposited(orderId, 100),
//                new AccountWithdrawn(orderId, 50)
//        );
//        dbEventStore.store(orderId, events);
//
//        // When
//        List<Event> newEvents = newArrayList(concat(events, singletonList(new OrderConfirmed(orderId))));
//        dbEventStore.store(orderId, newEvents);
//
//        // Then
//        List<Event> anotherNewEvents = newArrayList(concat(events, singletonList(new AccountWithdrawn(orderId, 10))));
//        assertThatThrownBy(() -> dbEventStore.store(orderId, anotherNewEvents))
//                .isInstanceOf(EventConcurrentUpdateException.class)
//                .hasMessageContaining("Failed to save events, version mismatch (there was a concurrent update)");
//    }
//
//    @Test
//    public void should_fail_to_store_when_version_is_very_old() {
//        // Given
//        orderId = OrderId.next();
//        List<Event> events = asList(
//                new OrderCreated(orderId, "toto", "1234-5678-9101"),
//                new AccountDeposited(orderId, 100),
//                new AccountDeposited(orderId, 200),
//                new AccountWithdrawn(orderId, 50)
//        );
//        dbEventStore.store(orderId, events);
//
//        // When
//        List<Event> anotherEventsLateVersion = asList(
//                new OrderCreated(orderId, "toto", "1234-5678-9101"),
//                new AccountDeposited(orderId, 500)
//        );
//
//        // Then
//        assertThatThrownBy(() -> dbEventStore.store(orderId, anotherEventsLateVersion))
//                .isInstanceOf(EventConcurrentUpdateException.class)
//                .hasMessageContaining("Failed to save events, version mismatch (there was a concurrent update)");
//    }
}
