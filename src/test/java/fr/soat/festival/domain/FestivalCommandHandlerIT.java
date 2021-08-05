package fr.soat.festival.domain;

import fr.soat.eventsourcing.configuration.DbEventStoreConfiguration;
import fr.soat.eventsourcing.impl.db.DBEventStore;
import fr.soat.festival.application.configuration.FestivalConfiguration;
import fr.soat.festival.domain.concert.ConcertRepository;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.concert.model.Concert;
import fr.soat.festival.domain.place.PlaceRepository;
import fr.soat.festival.domain.place.model.Place;
import fr.soat.festival.domain.place.model.PlaceId;
import fr.soat.festival.domain.spectator.AccountRepository;
import fr.soat.festival.domain.spectator.SpectatorRepository;
import fr.soat.festival.domain.spectator.SpectatorService;
import fr.soat.festival.domain.spectator.model.Account;
import fr.soat.festival.domain.spectator.model.Booking;
import fr.soat.festival.domain.spectator.model.Spectator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DbEventStoreConfiguration.class,
        FestivalConfiguration.class
})
@Transactional
class FestivalCommandHandlerIT {

    @Autowired
    DBEventStore<?,?> dbEventStore;

    @Autowired
    FestivalCommandHandler festivalCommandHandler;

    @Autowired
    ConcertRepository concertRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    SpectatorRepository spectatorRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SpectatorService spectatorService;

    private final Artist marcelEtOrchestra = Artist.named("Marcel & son orchestre");
    private final Artist skape = Artist.named("Skape");

    @BeforeEach
    void setUp() {
        dbEventStore.clear();
    }

    @Test
    public void should_create_and_open_a_new_concert() {
        // When
        festivalCommandHandler.openConcert(marcelEtOrchestra, 10, 3);

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
        festivalCommandHandler.openConcert(marcelEtOrchestra, 10, 3);
        Spectator spectator = spectatorService.createWithAccount(100);

        // When
        Booking booking = festivalCommandHandler.book(marcelEtOrchestra, spectator.getId());

        // Then
        assertThat(booking).isInstanceOf(Booking.RegisteredBooking.class);
        assertThat(booking.getArtist()).isEqualTo(marcelEtOrchestra);

        Place bookedPlace = placeRepository.load(booking.getPlaceId());
        assertThat(bookedPlace.getPrice()).isEqualTo(3);
        assertThat(bookedPlace.getStatus()).isEqualTo(Place.Status.ASSIGNED);
        assertThat(bookedPlace.getAssignee()).isEqualTo(spectator.getId());

        Concert reloadedConcert = concertRepository.load(marcelEtOrchestra);
        assertThat(reloadedConcert.getAvailablePlaces()).hasSize(9);
        assertThat(reloadedConcert.getStatus()).isEqualTo(Concert.Status.BOOKABLE);

        Spectator reloadedSpectator = spectatorRepository.load(spectator.getId());
        assertThat(reloadedSpectator.getBookings()).hasSize(1);
        Place spectatorPlace = placeRepository.load(reloadedSpectator.getBooking(marcelEtOrchestra).getPlaceId());
        assertThat(spectatorPlace).isEqualToIgnoringGivenFields(bookedPlace, "version");

        Account account = accountRepository.getOne(spectator.getId());
        assertThat(account.getBalance()).isEqualTo(97);
    }

    @Test
    void should_book_one_place_in_two_concert_not_full() {
        // Given
        festivalCommandHandler.openConcert(marcelEtOrchestra, 10, 3);
        festivalCommandHandler.openConcert(skape, 5, 2);
        Spectator spectator = spectatorService.createWithAccount(100);

        // When
        Booking marcelBooking = festivalCommandHandler.book(marcelEtOrchestra, spectator.getId());
        spectator = spectatorRepository.load(spectator.getId());
        Booking skapeBooking = festivalCommandHandler.book(skape, spectator.getId());

        // Then
        assertThat(marcelBooking).isInstanceOf(Booking.RegisteredBooking.class);
        assertThat(marcelBooking.getArtist()).isEqualTo(marcelEtOrchestra);
        Place marcelBookedPlace = placeRepository.load(marcelBooking.getPlaceId());
        assertThat(marcelBookedPlace.getArtist()).isEqualTo(marcelEtOrchestra);
        assertThat(marcelBookedPlace.getPrice()).isEqualTo(3);
        assertThat(marcelBookedPlace.getStatus()).isEqualTo(Place.Status.ASSIGNED);
        assertThat(marcelBookedPlace.getAssignee()).isEqualTo(spectator.getId());

        assertThat(skapeBooking).isInstanceOf(Booking.RegisteredBooking.class);
        assertThat(skapeBooking.getArtist()).isEqualTo(skape);
        Place skapeBookedPlace = placeRepository.load(skapeBooking.getPlaceId());
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
        Place spectatorMarcelPlace = placeRepository.load(reloadedSpectator.getBooking(marcelEtOrchestra).getPlaceId());
        assertThat(spectatorMarcelPlace).isEqualToIgnoringGivenFields(marcelBookedPlace, "version");
        Place spectatorSkapePlace = placeRepository.load(reloadedSpectator.getBooking(skape).getPlaceId());
        assertThat(spectatorSkapePlace).isEqualToIgnoringGivenFields(skapeBookedPlace, "version");

        Account account = accountRepository.getOne(spectator.getId());
        assertThat(account.getBalance()).isEqualTo(95);
    }

    @Test
    void should_refuse_booking_when_concert_is_full() {
        // Given
        festivalCommandHandler.openConcert(marcelEtOrchestra, 1, 3);
        Spectator earlySpectator = spectatorService.createWithAccount(100);
        Spectator lateSspectator = spectatorService.createWithAccount(100);

        // When
        Booking registeredBooking = festivalCommandHandler.book(marcelEtOrchestra, earlySpectator.getId());
        Booking rejectedBooking = festivalCommandHandler.book(marcelEtOrchestra, lateSspectator.getId());

        // Then
        assertThat(registeredBooking).isInstanceOf(Booking.RegisteredBooking.class);
        assertThat(rejectedBooking).isInstanceOf(Booking.RejectedBooking.class);

        Concert reloadedConcert = concertRepository.load(marcelEtOrchestra);
        assertThat(reloadedConcert.getAvailablePlaces()).hasSize(0);
        assertThat(reloadedConcert.getStatus()).isEqualTo(Concert.Status.FULL);

        Spectator reloadedEarlySpectator = spectatorRepository.load(earlySpectator.getId());
        assertThat(reloadedEarlySpectator.getBookings()).hasSize(1);
        Place earlySpectatorPlace = placeRepository.load(reloadedEarlySpectator.getBooking(marcelEtOrchestra).getPlaceId());
        assertThat(earlySpectatorPlace.getId()).isEqualTo(registeredBooking.getPlaceId());
        Account earlySpectatorAccount = accountRepository.getOne(earlySpectator.getId());
        assertThat(earlySpectatorAccount.getBalance()).isEqualTo(97);

        Spectator reloadedLateSpectator = spectatorRepository.load(lateSspectator.getId());
        assertThat(reloadedLateSpectator.getBooking(marcelEtOrchestra)).isInstanceOf(Booking.RejectedBooking.class);
        Account lateSpectatorAccount = accountRepository.getOne(lateSspectator.getId());
        assertThat(lateSpectatorAccount.getBalance()).isEqualTo(100);
    }

    @Test
    void should_cancel_a_booking() {
        // Given
        festivalCommandHandler.openConcert(marcelEtOrchestra, 10, 3);
        Spectator spectator = spectatorService.createWithAccount(100);
        PlaceId bookedPlaceId = festivalCommandHandler.book(marcelEtOrchestra, spectator.getId()).getPlaceId();

        // When
        festivalCommandHandler.cancelBooking(bookedPlaceId);

        // Then
        Concert concert = concertRepository.load(marcelEtOrchestra);
        assertThat(concert.getAvailablePlaces()).hasSize(10);

        Place bookedPlace = placeRepository.load(bookedPlaceId);
        assertThat(bookedPlace.getStatus()).isEqualTo(Place.Status.AVAILABLE);
        assertThat(bookedPlace.getAssignee()).isNull();

        Spectator reloadedSpectator = spectatorRepository.load(spectator.getId());
        assertThat(reloadedSpectator.getBookings()).isEmpty();

        Account account = accountRepository.getOne(spectator.getId());
        assertThat(account.getBalance()).isEqualTo(100);
    }
}
