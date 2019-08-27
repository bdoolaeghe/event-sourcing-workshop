package fr.soat.conference.domain;

import fr.soat.conference.application.configuration.ConferenceManagementConfig;
import fr.soat.conference.domain.booking.Conference;
import fr.soat.conference.domain.booking.ConferenceName;
import fr.soat.conference.domain.order.Order;
import fr.soat.conference.domain.order.OrderId;
import fr.soat.conference.domain.payment.Account;
import fr.soat.conference.domain.payment.AccountId;
import fr.soat.conference.infra.booking.ConferenceRepository;
import fr.soat.conference.infra.order.OrderRepository;
import fr.soat.conference.infra.payment.AccountRepository;
import fr.soat.eventsourcing.api.EventStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static fr.soat.conference.domain.order.OrderStatus.CONFIRMED;
import static fr.soat.conference.domain.order.OrderStatus.REFUSED;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ConferenceManagementConfig.class)
public class ConferenceBookingServiceTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ConferenceRepository conferenceRepository;

    @Autowired
    ConferenceBookingService conferenceBookingService;

    @Autowired
    EventStore eventStore;

    @Before
    public void setUp() throws Exception {
        eventStore.clear();
    }

    @Test
    public void should_book_a_seat_successfully() {
        // Given
        AccountId myAccountId = AccountId.next();
        Account myAccount = new Account(myAccountId).credit(100);
        accountRepository.save(myAccount);

        ConferenceName conferenceName = ConferenceName.name("La stratégie de l'echec");
        Conference conference = new Conference(conferenceName).open(10, 15);
        conferenceRepository.save(conference);

        // When
        OrderId orderId = conferenceBookingService.requestOrder(conferenceName, myAccountId);

        // Then
        Order order = orderRepository.load(orderId);
        assertThat(order.getStatus()).isEqualTo(CONFIRMED);
        assertThat(order.getSeat().getPlaceNumber()).isEqualTo(1);

        myAccount = accountRepository.load(myAccountId);
        assertThat(myAccount.getBalance()).isEqualTo(85);

        conference = conferenceRepository.load(conferenceName);
        assertThat(conference.getAvailableSeats().size()).isEqualTo(9);
    }

    @Test
    public void should_cancel_booking_if_funds_are_insufficient() {
        // Given
        AccountId myAccountId = AccountId.next();
        Account myAccount = new Account(myAccountId).credit(3);
        accountRepository.save(myAccount);

        ConferenceName conferenceName = ConferenceName.name("La stratégie de l'echec");
        Conference conference = new Conference(conferenceName).open(10, 15);
        conferenceRepository.save(conference);

        // When
        OrderId orderId = conferenceBookingService.requestOrder(conferenceName, myAccountId);

        // Then
        Order order = orderRepository.load(orderId);
        assertThat(order.getStatus()).isEqualTo(REFUSED);
        assertThat(order.getSeat()).isNull();

        myAccount = accountRepository.load(myAccountId);
        assertThat(myAccount.getBalance()).isEqualTo(3);

        conference = conferenceRepository.load(conferenceName);
        assertThat(conference.getAvailableSeats().size()).isEqualTo(10);
    }

    @Test
    public void should_cancel_booking_if_the_is_place_no_more() {
        // Given
        AccountId myAccountId = AccountId.next();
        Account myAccount = new Account(myAccountId).credit(100);
        accountRepository.save(myAccount);

        ConferenceName conferenceName = ConferenceName.name("La stratégie de l'echec");
        Conference conference = new Conference(conferenceName).open(1, 15);
        conferenceRepository.save(conference);

        // When
        OrderId orderId1 = conferenceBookingService.requestOrder(conferenceName, myAccountId);
        OrderId orderId2 = conferenceBookingService.requestOrder(conferenceName, myAccountId);

        // Then
        Order order1 = orderRepository.load(orderId1);
        Order order2 = orderRepository.load(orderId2);
        assertThat(order1.getStatus()).isEqualTo(CONFIRMED);
        assertThat(order1.getSeat().getPlaceNumber()).isEqualTo(1);
        assertThat(order2.getStatus()).isEqualTo(REFUSED);
        assertThat(order2.getSeat()).isNull();

        myAccount = accountRepository.load(myAccountId);
        assertThat(myAccount.getBalance()).isEqualTo(100 - 15);

        conference = conferenceRepository.load(conferenceName);
        assertThat(conference.getAvailableSeats().size()).isEqualTo(0);
    }

    //TODO test on Account aggregate and Conference
    // Faut il qu'on ait 2 types d'event pour SeatBooked et SeatAssgined ?
    //TODO : enregister une ref paiement dans l'Order
}
