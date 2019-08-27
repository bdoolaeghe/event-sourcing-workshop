package fr.soat.conference.domain.order;

import fr.soat.conference.domain.booking.ConferenceName;
import fr.soat.conference.domain.booking.Seat;
import fr.soat.conference.domain.payment.AccountId;
import fr.soat.conference.domain.payment.PaymentReference;
import fr.soat.conference.infra.order.OrderRepository;
import fr.soat.eventsourcing.impl.InMemoryEventStore;
import fr.soat.eventsourcing.impl.NOOPEventPublisher;
import org.junit.Test;

import static fr.soat.conference.domain.order.OrderStatus.PAID;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    ConferenceName conferenceName = ConferenceName.name("picasso dans tous ses etats");
    AccountId myAccountId = AccountId.next();
    OrderRepository repository = new OrderRepository(new InMemoryEventStore(new NOOPEventPublisher()));
    private Seat seatOne = new Seat(1);
    private PaymentReference  paymentReference = PaymentReference.genereate();

    @Test
    public void should_create_book_then_pay_order() {
        Order order = OrderFactory.create();
        order.requestBooking(conferenceName, myAccountId)
                .assign(seatOne)
                .confirmPayment(paymentReference);
        repository.save(order);

        order = repository.load(order.getId());
        assertThat(order.getStatus()).isEqualTo(PAID);
        assertThat(order.getSeat()).isEqualTo(seatOne);
        assertThat(order.getPaymentReference()).isEqualTo(paymentReference);
    }

}
