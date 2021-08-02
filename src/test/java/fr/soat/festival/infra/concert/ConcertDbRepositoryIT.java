package fr.soat.festival.infra.concert;

import fr.soat.eventsourcing.configuration.DbEventStoreConfiguration;
import fr.soat.festival.application.configuration.FestivalConfiguration;
import fr.soat.festival.domain.concert.ConcertRepository;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.concert.model.Concert;
import fr.soat.festival.domain.place.model.PlaceId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DbEventStoreConfiguration.class,
        FestivalConfiguration.class
})
@Transactional
class ConcertDbRepositoryIT {

    @Autowired
    ConcertRepository concertRepository;

    Artist anArtist = Artist.named("Marcel & son orchestre");

    @Test
    public void should_store_and_reload_concert() {
        // Given
        Concert concert = Concert.create(anArtist)
                .assignRoom(singletonList(PlaceId.from("place_test_id")));

        // When
        Concert savedConcert = concertRepository.save(concert);
        Concert reloadedConcert = concertRepository.load(savedConcert.getId());

        // Then
        assertThat(reloadedConcert).isEqualToIgnoringGivenFields(concert, "version");
    }


}
