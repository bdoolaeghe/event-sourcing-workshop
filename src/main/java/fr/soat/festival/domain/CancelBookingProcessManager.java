package fr.soat.festival.domain;

import fr.soat.festival.domain.concert.ConcertRepository;
import fr.soat.festival.domain.concert.model.Concert;
import fr.soat.festival.domain.place.model.PlaceAssignmentCanceled;
import fr.soat.festival.domain.spectator.SpectatorRepository;
import fr.soat.festival.domain.spectator.model.Spectator;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CancelBookingProcessManager {

    ConcertRepository concertRepository;
    SpectatorRepository spectatorRepository;

    @EventListener
    public void on(PlaceAssignmentCanceled event) {
        // update the spectator bookings
        Spectator spectator = spectatorRepository.load(event.getSpectatorId());
        spectator = spectator.cancelBooking(event.getArtist());
        spectatorRepository.save(spectator);

        // update the concert (make the place available back) + update status
        Concert concert = concertRepository.load(event.getArtist());
        concert = concert.cancelBooking(event.getPlaceId());
        concertRepository.save(concert);
    }

}
