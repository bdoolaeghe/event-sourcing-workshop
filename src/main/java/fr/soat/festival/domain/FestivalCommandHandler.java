package fr.soat.festival.domain;

import fr.soat.eventsourcing.api.Command;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.place.model.PlaceId;
import fr.soat.festival.domain.spectator.model.Booking;
import fr.soat.festival.domain.spectator.model.SpectatorId;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Command
public class FestivalCommandHandler {

    private final FestivalOrganizationService festivalOrganizationService;
    private final BookingService bookingService;

    public void openConcert(Artist artist, int places, int price) {
        festivalOrganizationService.openConcert(artist, places, price);
    }

    public Booking book(Artist artist, SpectatorId spectatorId) {
        return bookingService.book(artist, spectatorId);
    }

    public void cancelBooking(PlaceId placeId) {
        bookingService.cancelBooking(placeId);
    }

}
