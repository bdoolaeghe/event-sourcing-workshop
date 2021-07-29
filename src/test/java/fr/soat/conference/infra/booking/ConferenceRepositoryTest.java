package fr.soat.conference.infra.booking;

import fr.soat.conference.application.configuration.ConferenceConfiguration;
import fr.soat.conference.domain.booking.Conference;
import fr.soat.conference.domain.booking.ConferenceName;
import fr.soat.conference.domain.order.OrderId;
import fr.soat.eventsourcing.configuration.DbEventStoreConfiguration;
import fr.soat.eventsourcing.impl.db.DBEventStore;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        DbEventStoreConfiguration.class,
        ConferenceConfiguration.class
})
@Transactional
public class ConferenceRepositoryTest extends TestCase {

    @Autowired
    DBEventStore dbEventStore;

    @Autowired
    ConferenceRepository conferenceRepository;

    OrderId anOrderId = OrderId.from("1234");
    ConferenceName aConferenceName = ConferenceName.name("La stratégie de l'echec");
//    AccountId anAccountId = AccountId.from("1234");
//    PaymentReference aPaymentReference = PaymentReference.from("7890");
//    Seat aSeat = Seat.builder().placeNumber(13).build();

    @Before
    public void setUp() throws Exception {
        dbEventStore.clear();
    }

    @Test
    public void should_store_and_reload_conference() {
        // Given
        Conference conference = Conference.create(aConferenceName)
                .open(100, 6);
//        conference.bookSeat(anOrderId);

        // When
        Conference savedConference = conferenceRepository.save(conference);
        Conference reloadedConference = conferenceRepository.load(savedConference.getId());

        // Then
        assertThat(reloadedConference).isEqualTo(conference);
    }


}
