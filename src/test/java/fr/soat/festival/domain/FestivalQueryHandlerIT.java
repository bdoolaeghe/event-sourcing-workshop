package fr.soat.festival.domain;

import fr.soat.eventsourcing.configuration.DbEventStoreConfiguration;
import fr.soat.festival.application.configuration.FestivalConfiguration;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.dashboard.model.ConcertDashboard;
import fr.soat.festival.domain.spectator.SpectatorRepository;
import fr.soat.festival.domain.spectator.model.Spectator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DbEventStoreConfiguration.class,
        FestivalConfiguration.class
})
@Transactional
class FestivalQueryHandlerIT {

    Artist marcelEtSonOchestre = Artist.named("marcel & son orchestre");

    @Autowired
    FestivalQueryHandler festivalQueryHandler;
    @Autowired
    FestivalCommandHandler festivalCommandHandler;
    @Autowired
    SpectatorRepository spectatorRepository;

    @Test
    void should_live_compute_concert_dashboard_after_some_bookings() {
        // Given
        festivalCommandHandler.openConcert(marcelEtSonOchestre, 100, 7);
        Spectator roger = spectatorRepository.save(Spectator.create());
        Spectator kevin = spectatorRepository.save(Spectator.create());

        // When
        festivalCommandHandler.book(marcelEtSonOchestre, roger.getId());
        festivalCommandHandler.book(marcelEtSonOchestre, kevin.getId());

        // Then
        ConcertDashboard dashboard = festivalQueryHandler.getConcertDashboard(marcelEtSonOchestre);
        assertThat(dashboard).isEqualTo(new ConcertDashboard(marcelEtSonOchestre, "2%", "14 euro"));
    }

    @Test
    void should_live_compute_concert_dashboard_after_some_booking_cancellations() {
        // Given
        festivalCommandHandler.openConcert(marcelEtSonOchestre, 100, 7);
        Spectator roger = spectatorRepository.save(Spectator.create());
        Spectator kevin = spectatorRepository.save(Spectator.create());
        festivalCommandHandler.book(marcelEtSonOchestre, roger.getId());
        festivalCommandHandler.book(marcelEtSonOchestre, kevin.getId());

        // When
        roger = spectatorRepository.load(roger.getId());
        festivalCommandHandler.cancelBooking(roger.getBooking(marcelEtSonOchestre).getPlaceId());

        // Then
        ConcertDashboard dashboard = festivalQueryHandler.getConcertDashboard(marcelEtSonOchestre);
        assertThat(dashboard).isEqualTo(new ConcertDashboard(marcelEtSonOchestre, "1%", "7 euro"));
    }
}
