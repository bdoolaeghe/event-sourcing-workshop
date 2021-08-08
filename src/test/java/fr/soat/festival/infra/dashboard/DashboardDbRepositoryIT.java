package fr.soat.festival.infra.dashboard;

import fr.soat.eventsourcing.configuration.DbEventStoreConfiguration;
import fr.soat.festival.application.configuration.FestivalConfiguration;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.dashboard.DashboardRepository;
import fr.soat.festival.domain.dashboard.model.ConcertDashboard;
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
class DashboardDbRepositoryIT {

    @Autowired
    DashboardRepository dashboardRepository;

    Artist anArtist = Artist.named("Marcel & son orchestre");

    @Test
    void should_create_and_get_dashboard_() {
        // When
        dashboardRepository.createConcertDashboard(anArtist);
        ConcertDashboard dashboard = dashboardRepository.getConcertDashboard(anArtist);

        // Then
        assertThat(dashboard).isEqualTo(new ConcertDashboard(anArtist, "0%", "0 euro"));
    }

    @Test
    void should_add_incomes() {
        //Given
        dashboardRepository.createConcertDashboard(anArtist);

        // When
        dashboardRepository.addIncomes(anArtist, 10);
        dashboardRepository.addIncomes(anArtist, 20);

        // Then
        ConcertDashboard dashboard = dashboardRepository.getConcertDashboard(anArtist);
        assertThat(dashboard).isEqualTo(new ConcertDashboard(anArtist, "0%", "30 euro"));
    }

    @Test
    void should_remove_incomes() {
        //Given
        dashboardRepository.createConcertDashboard(anArtist);

        // When
        dashboardRepository.addIncomes(anArtist, 20);
        dashboardRepository.removeIncomes(anArtist, 5);

        // Then
        ConcertDashboard dashboard = dashboardRepository.getConcertDashboard(anArtist);
        assertThat(dashboard).isEqualTo(new ConcertDashboard(anArtist, "0%", "15 euro"));
    }

    @Test
    void should_update_booking_ratios() {
        //Given
        dashboardRepository.createConcertDashboard(anArtist);

        // When
        dashboardRepository.updateBookingRatio(anArtist, 33);

        // Then
        ConcertDashboard dashboard = dashboardRepository.getConcertDashboard(anArtist);
        assertThat(dashboard).isEqualTo(new ConcertDashboard(anArtist, "33%", "0 euro"));
    }
}
