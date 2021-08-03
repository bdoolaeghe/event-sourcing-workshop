package fr.soat.festival.domain;

import fr.soat.eventsourcing.configuration.DbEventStoreConfiguration;
import fr.soat.eventsourcing.impl.db.DBEventStore;
import fr.soat.festival.application.configuration.FestivalConfiguration;
import fr.soat.festival.domain.concert.ConcertRepository;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.concert.model.Concert;
import fr.soat.festival.domain.place.PlaceRepository;
import fr.soat.festival.domain.place.model.Place;
import fr.soat.festival.domain.spectator.SpectatorRepository;
import fr.soat.festival.domain.spectator.model.Spectator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DbEventStoreConfiguration.class,
        FestivalConfiguration.class
})
@Transactional
class FestivalCommandHandlerIT {

    @Autowired
    DBEventStore dbEventStore;

    @Autowired
    FestivalCommandHandler festivalCommandHandler;

    @Autowired
    ConcertRepository concertRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    SpectatorRepository spectatorRepository;

    private final Artist marcelEtOrchestra = Artist.named("Marcel & son orchestre");
    private final Artist skape = Artist.named("Skape");

    @BeforeEach
    void setUp() {
        dbEventStore.clear();
    }

    @Test
    public void should_create_and_open_a_new_concert() {
        // When
        Concert concert = festivalCommandHandler.openConcert(marcelEtOrchestra, 10, 3);

        // Then
        Concert savedConcert = concertRepository.load(marcelEtOrchestra);
        assertThat(savedConcert.getArtist()).isEqualTo(Artist.named("Marcel & son orchestre"));
        assertThat(savedConcert.getStatus()).isEqualTo(Concert.Status.BOOKABLE);
        assertThat(savedConcert.getAvailablePlaces()).hasSize(10);

        List<Place> room = placeRepository.load(savedConcert.getAvailablePlaces());
        assertThat(room).extracting(Place::getPrice).containsOnly(3);
        assertThat(room).extracting(Place::getArtist).containsOnly(marcelEtOrchestra);
        assertThat(room).extracting(Place::getStatus).containsOnly(Place.Status.AVAILABLE);
    }

    @Test
    void should_book_a_place_when_concert_is_not_full() {
        // Given
        Concert concert = festivalCommandHandler.openConcert(marcelEtOrchestra, 10, 3);
        Spectator spectator = Spectator.create();

        // When
        Place bookedPlace = festivalCommandHandler.book(marcelEtOrchestra, spectator).get();

        // Then
        assertThat(bookedPlace.getArtist()).isEqualTo(marcelEtOrchestra);
        assertThat(bookedPlace.getPrice()).isEqualTo(3);
        assertThat(bookedPlace.getStatus()).isEqualTo(Place.Status.ASSIGNED);
        assertThat(bookedPlace.getAssignee()).isEqualTo(spectator.getId());

        Concert reloadedConcert = concertRepository.load(marcelEtOrchestra);
        assertThat(reloadedConcert.getAvailablePlaces()).hasSize(9);
        assertThat(reloadedConcert.getStatus()).isEqualTo(Concert.Status.BOOKABLE);

        Spectator reloadedSpectator = spectatorRepository.load(spectator.getId());
        assertThat(reloadedSpectator.getBookings()).hasSize(1);
        Place spectatorPlace = placeRepository.load(reloadedSpectator.getBookings().get(0));
        assertThat(spectatorPlace).isEqualToIgnoringGivenFields(bookedPlace, "version");
    }

    @Test
    void should_book_one_place_in_two_concert_not_full() {
        // Given
        Concert concertMarcel = festivalCommandHandler.openConcert(marcelEtOrchestra, 10, 3);
        Concert concertSkape = festivalCommandHandler.openConcert(skape, 5, 2);
        Spectator spectator = spectatorRepository.save(Spectator.create());

        // When
        Place marcelBookedPlace = festivalCommandHandler.book(marcelEtOrchestra, spectator).get();
        spectator = spectatorRepository.load(spectator.getId());
        Place skapeBookedPlace = festivalCommandHandler.book(skape, spectator).get();

        // Then
        assertThat(marcelBookedPlace.getArtist()).isEqualTo(marcelEtOrchestra);
        assertThat(marcelBookedPlace.getPrice()).isEqualTo(3);
        assertThat(marcelBookedPlace.getStatus()).isEqualTo(Place.Status.ASSIGNED);
        assertThat(marcelBookedPlace.getAssignee()).isEqualTo(spectator.getId());
        assertThat(skapeBookedPlace.getArtist()).isEqualTo(skape);
        assertThat(skapeBookedPlace.getPrice()).isEqualTo(2);
        assertThat(skapeBookedPlace.getStatus()).isEqualTo(Place.Status.ASSIGNED);
        assertThat(skapeBookedPlace.getAssignee()).isEqualTo(spectator.getId());

        Concert reloadedMarcelConcert = concertRepository.load(marcelEtOrchestra);
        assertThat(reloadedMarcelConcert.getAvailablePlaces()).hasSize(9);
        assertThat(reloadedMarcelConcert.getStatus()).isEqualTo(Concert.Status.BOOKABLE);
        Concert reloadedSkapeConcert = concertRepository.load(skape);
        assertThat(reloadedSkapeConcert.getAvailablePlaces()).hasSize(4);
        assertThat(reloadedSkapeConcert.getStatus()).isEqualTo(Concert.Status.BOOKABLE);

        Spectator reloadedSpectator = spectatorRepository.load(spectator.getId());
        assertThat(reloadedSpectator.getBookings()).hasSize(2);
        Place spectatorMarcelPlace = placeRepository.load(reloadedSpectator.getBookings().get(0));
        assertThat(spectatorMarcelPlace).isEqualToIgnoringGivenFields(marcelBookedPlace, "version");
        Place spectatorSkapePlace = placeRepository.load(reloadedSpectator.getBookings().get(1));
        assertThat(spectatorSkapePlace).isEqualToIgnoringGivenFields(skapeBookedPlace, "version");
    }

    @Test
    void should_refuse_booking_when_concert_is_full() {
        // Given
        Concert concert = festivalCommandHandler.openConcert(marcelEtOrchestra, 1, 3);
        Spectator earlySpectator = spectatorRepository.save(Spectator.create());
        Spectator lateSspectator = spectatorRepository.save(Spectator.create());

        // When
        Optional<Place> bookedPlace = festivalCommandHandler.book(marcelEtOrchestra, earlySpectator);
        Optional<Place> notBookedPlace = festivalCommandHandler.book(marcelEtOrchestra, lateSspectator);

        // Then
        assertThat(bookedPlace).isNotEmpty();
        assertThat(notBookedPlace).isEmpty();

        Concert reloadedConcert = concertRepository.load(marcelEtOrchestra);
        assertThat(reloadedConcert.getAvailablePlaces()).hasSize(0);
        assertThat(reloadedConcert.getStatus()).isEqualTo(Concert.Status.FULL);

        Spectator reloadedEarlySpectator = spectatorRepository.load(earlySpectator.getId());
        assertThat(reloadedEarlySpectator.getBookings()).hasSize(1);
        Place earlySpectatorPlace = placeRepository.load(reloadedEarlySpectator.getBookings().get(0));
        assertThat(earlySpectatorPlace).isEqualToIgnoringGivenFields(bookedPlace.get(), "version");
        Spectator reloadedLateSpectator = spectatorRepository.load(lateSspectator.getId());
        assertThat(reloadedLateSpectator.getBookings()).isEmpty();
    }

    @Test
    void should_cancel_a_booking() {
        // Given
        Concert concert = festivalCommandHandler.openConcert(marcelEtOrchestra, 10, 3);
        Spectator spectator = spectatorRepository.save(Spectator.create());
        Place bookedPlace = festivalCommandHandler.book(marcelEtOrchestra, spectator).get();

        // When
        festivalCommandHandler.cancelBooking(bookedPlace.getId());

        // Then
        concert = concertRepository.load(marcelEtOrchestra);
        assertThat(concert.getAvailablePlaces()).hasSize(10);

        bookedPlace = placeRepository.load(bookedPlace.getId());
        assertThat(bookedPlace.getStatus()).isEqualTo(Place.Status.AVAILABLE);
        assertThat(bookedPlace.getAssignee()).isNull();

        Spectator reloadedSpectator = spectatorRepository.load(spectator.getId());
        assertThat(reloadedSpectator.getBookings()).isEmpty();
    }
}
