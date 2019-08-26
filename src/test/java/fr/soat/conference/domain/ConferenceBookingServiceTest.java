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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

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
        assertThat(order.isConfirmed()).isTrue();
    }

    //TODO test on failures case
    //TODO test on Account aggregate and Conference
}
