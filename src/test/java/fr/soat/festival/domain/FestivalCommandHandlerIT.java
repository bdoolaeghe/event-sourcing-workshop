package fr.soat.festival.domain;

import fr.soat.eventsourcing.configuration.DbEventStoreConfiguration;
import fr.soat.festival.application.configuration.ConferenceConfiguration;
import fr.soat.festival.domain.concert.ConcertRepository;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.concert.model.Concert;
import fr.soat.festival.domain.place.PlaceRepository;
import fr.soat.festival.domain.place.model.Place;
import fr.soat.festival.infra.place.PlaceDbRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DbEventStoreConfiguration.class,
        ConferenceConfiguration.class
})
@Transactional
class FestivalCommandHandlerIT {

    @Autowired
    FestivalCommandHandler festivalCommandHandler;

    @Autowired
    ConcertRepository concertRepository;

    @Autowired
    PlaceRepository placeRepository;

    private Artist anArtist = Artist.named("Marcel & son orchestre");

    @Test
    public void should_create_and_open_a_new_concert() {
        // When
        Concert concert = festivalCommandHandler.openConcert(anArtist, 10, 3);

        // Then
        Concert savedConcert = concertRepository.load(anArtist);
        assertThat(savedConcert.getArtist()).isEqualTo(Artist.named("Marcel & son orchestre"));
        assertThat(savedConcert.getStatus()).isEqualTo(Concert.Status.BOOKABLE);
        assertThat(savedConcert.getAvailablePlaces()).hasSize(10);

        List<Place> room = placeRepository.load(savedConcert.getAvailablePlaces());
        assertThat(room).extracting(Place::getPrice).containsOnly(3);
        assertThat(room).extracting(Place::getArtist).containsOnly(Artist.named("Marcel & son orchestre"));
        assertThat(room).extracting(Place::getStatus).containsOnly(Place.Status.AVAILABLE);
    }

}
