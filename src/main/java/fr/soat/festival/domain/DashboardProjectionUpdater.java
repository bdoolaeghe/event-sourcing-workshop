package fr.soat.festival.domain;

import fr.soat.festival.domain.concert.ConcertRepository;
import fr.soat.festival.domain.concert.model.*;
import fr.soat.festival.domain.dashboard.DashboardRepository;
import fr.soat.festival.domain.place.PlaceRepository;
import fr.soat.festival.domain.place.model.Place;
import fr.soat.festival.domain.place.model.PlaceId;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DashboardProjectionUpdater {

    ConcertRepository concertRepository;
    DashboardRepository dashboardRepository;
    PlaceRepository placeRepository;

    @EventListener
    public void on(ConcertOpeningRequested event) {
        // init the dashboards
        Artist artist = event.getArtist();
        dashboardRepository.createConcertDashboard(event.getArtist());
    }

    @EventListener
    public void on(ConcertPlaceBooked event) {
        Artist artist = event.getArtist();
        Concert concert = concertRepository.load(artist);

        int bookingRatio = computeBookingRatio(concert);
        dashboardRepository.updateBookingRatio(artist, bookingRatio);

        PlaceId bookedPlaceId = event.getBookedPlaceId();
        Place bookedPlace = placeRepository.load(bookedPlaceId);
        int incomes = bookedPlace.getPrice();
        dashboardRepository.addIncomes(artist, incomes);
    }

    private int computeBookingRatio(Concert concert) {
        int roomSize = concert.getRoomSize();
        int availablePlaces = concert.getAvailablePlaces().size();
        return Math.round(
                100 - (100 * (float) availablePlaces / (float) roomSize)
        );
    }

    @EventListener
    public void on(ConcertPlaceBookingCanceled event) {
        Place place = placeRepository.load(event.getPlaceId());
        Artist artist = place.getArtist();
        Concert concert = concertRepository.load(artist);

        int bookingRatio = computeBookingRatio(concert);
        dashboardRepository.updateBookingRatio(artist, bookingRatio);

        int incomes = place.getPrice();
        dashboardRepository.removeIncomes(artist, incomes);
    }

}
