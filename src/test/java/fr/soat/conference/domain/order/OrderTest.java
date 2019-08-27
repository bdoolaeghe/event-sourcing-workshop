package fr.soat.conference.domain.order;

import fr.soat.conference.domain.booking.ConferenceName;
import fr.soat.conference.domain.payment.AccountId;
import fr.soat.conference.infra.order.OrderRepository;
import fr.soat.eventsourcing.impl.InMemoryEventStore;
import fr.soat.eventsourcing.impl.NOOPEventPublisher;
import org.junit.Test;

public class OrderTest {

    ConferenceName conferenceName = ConferenceName.name("picasso dans tous ses etats");
    AccountId myAccountId = AccountId.next();
    OrderRepository repository = new OrderRepository(new InMemoryEventStore(new NOOPEventPublisher()));

    @Test
    public void should_create_book_then_release_seat() {
        Order order = OrderFactory.create();
//        order.requestSeat(conferenceName, myAccountId)
//        .assign(new Seat(1))
//                .confirmRequest();
//                .
//
//
//        Conference picasso = new Conference(conferenceName).open(3, 10);
//        Seat seat1 = picasso.bookSeat(OrderId.next()).get();
//        Seat seat2 = picasso.bookSeat(OrderId.next()).get();
//        Seat seat3 = picasso.bookSeat(OrderId.next()).get();
//
//        assertThat(picasso.getAvailableSeats()).isEmpty();
//        picasso.cancelBooking(seat2);
//
//        repository.save(picasso);
//        Conference reloadedPicasso = repository.load(picasso.getId());
//        assertThat(picasso.getAvailableSeats()).containsExactly(seat2);
    }

    @Test
    public void should_return_empty_when_booking_with_no_more_places() {
//        Conference picasso = new Conference(ConferenceName.name("picasso dans tous ses etats")).open(3, 10);
//        Seat seat1 = picasso.bookSeat(OrderId.next()).get();
//        Seat seat2 = picasso.bookSeat(OrderId.next()).get();
//        Seat seat3 = picasso.bookSeat(OrderId.next()).get();
//
//        assertThat(picasso.bookSeat(OrderId.next())).isEmpty();
    }

}
