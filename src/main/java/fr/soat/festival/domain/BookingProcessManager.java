package fr.soat.festival.domain;

import fr.soat.festival.domain.concert.ConcertRepository;
import fr.soat.festival.domain.concert.model.ConcertPlaceBooked;
import fr.soat.festival.domain.concert.model.ConcertPlaceBookingRequestRejected;
import fr.soat.festival.domain.place.PlaceRepository;
import fr.soat.festival.domain.place.model.Place;
import fr.soat.festival.domain.place.model.PlaceId;
import fr.soat.festival.domain.spectator.SpectatorRepository;
import fr.soat.festival.domain.spectator.model.Spectator;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookingProcessManager {

    ConcertRepository concertRepository;
    PlaceRepository placeRepository;
    SpectatorRepository spectatorRepository;

    @EventListener
    public void on(ConcertPlaceBookingRequestRejected event) {
        // register booking rejection in spectator
        Spectator spectator = spectatorRepository.load(event.getSpectatorId());
        spectator = spectator.rejectBooking(event.getArtist());
        spectatorRepository.save(spectator);
    }

    @EventListener
    public void on(ConcertPlaceBooked event) {
        // Assign place to spectator
        PlaceId bookedPlaceId = event.getBookedPlaceId();
        Place bookedPlace = placeRepository.load(bookedPlaceId);
        bookedPlace = bookedPlace.assignTo(event.getSpectatorId());
        placeRepository.save(bookedPlace);

        // register place in spectator bookings
        Spectator spectator = spectatorRepository.load(event.getSpectatorId());
        spectator = spectator.registerBooking(bookedPlaceId, event.getArtist());
        spectatorRepository.save(spectator);
    }

}
