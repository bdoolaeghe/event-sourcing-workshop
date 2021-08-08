package fr.soat.festival.domain.dashboard;

import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.dashboard.model.ConcertDashboard;

public interface DashboardRepository {
    void createConcertDashboard(Artist artist);
    void updateBookingRatio(Artist artist, int newBookingRatio);
    void addIncomes(Artist artist, int incomesToAdd);
    void removeIncomes(Artist artist, int incomesToAdd);
    ConcertDashboard getConcertDashboard(Artist artist);
}
