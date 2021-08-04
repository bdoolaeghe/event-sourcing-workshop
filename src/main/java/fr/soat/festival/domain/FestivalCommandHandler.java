package fr.soat.festival.domain;

import fr.soat.festival.domain.concert.ConcertRepository;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.concert.model.Concert;
import fr.soat.festival.domain.place.PlaceRepository;
import fr.soat.festival.domain.place.model.Place;
import fr.soat.festival.domain.place.model.PlaceId;
import fr.soat.festival.domain.spectator.SpectatorRepository;
import fr.soat.festival.domain.spectator.model.Booking;
import fr.soat.festival.domain.spectator.model.SpectatorId;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class FestivalCommandHandler {

    private final ConcertRepository concertRepository;
    private final PlaceRepository placeRepository;
    private final SpectatorRepository spectatorRepository;

    @Transactional
    public void openConcert(Artist artist, int places, int price) {
        Concert concert = Concert
                .create(artist)
                .open(places, price);
        concertRepository.save(concert);
    }

    @Transactional
    public Booking book(Artist artist, SpectatorId spectatorId) {
        Concert concert = concertRepository.load(artist);
        concert = concert.requestBooking(artist, spectatorId);
        concertRepository.save(concert);
        return spectatorRepository.load(spectatorId)
                .getBooking(artist);
    }

    @Transactional
    public void cancelBooking(PlaceId placeId) {
        Place place = placeRepository.load(placeId);
        place = place.cancelBooking();
        placeRepository.save(place);
    }

}
