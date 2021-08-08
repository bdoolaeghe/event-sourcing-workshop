package fr.soat.festival.domain;

import fr.soat.eventsourcing.api.Query;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.dashboard.DashboardRepository;
import fr.soat.festival.domain.dashboard.model.ConcertDashboard;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Query
@AllArgsConstructor
public class FestivalQueryHandler {

    DashboardRepository dashboardRepository;

    public ConcertDashboard getConcertDashboard(Artist artist) {
        return dashboardRepository.getConcertDashboard(artist);
    }

}
