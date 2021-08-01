package fr.soat.festival.domain;

import fr.soat.festival.domain.concert.ConcertRepository;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.concert.model.Concert;
import fr.soat.festival.domain.place.PlaceRepository;
import fr.soat.festival.domain.spectator.SpectatorRepository;
import fr.soat.festival.domain.spectator.model.Spectator;
import fr.soat.festival.domain.place.model.Place;
import fr.soat.festival.domain.place.model.PlaceId;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class FestivalCommandHandler {

    private final ConcertRepository concertRepository;
    private final PlaceRepository placeRepository;
    private final SpectatorRepository spectatorRepository;

    @Transactional
    public Concert openConcert(Artist artist, int places, int price) {
        // create concert
        Concert concert = concertRepository.save(Concert.create(artist));

        // creates places
        List<PlaceId> room = IntStream.range(0, places)
                .mapToObj(i -> Place.create().allocateTo(artist, price))
                .map(placeRepository::save)
                .map(Place::getId)
                .collect(toList());

        // then attach to new created concert
        return concertRepository.save(concert.assignRoom(room));
    }

    @Transactional
    public Optional<Place> book(Artist artist, Spectator spectator) {
        // lookup an available place
        Concert concert = concertRepository.load(artist);
        if (concert.isFull()) {
            spectator = spectator.rejectBooking(artist);
            spectatorRepository.save(spectator);
            return Optional.empty();
        } else {
            PlaceId placeId = concert.getAnAvailablePlaceId();
            concert = concert.book(placeId);
            concertRepository.save(concert);
            Place place = placeRepository.load(placeId);
            place = place.assignTo(spectator.getId());
            placeRepository.save(place);
            spectator = spectator.registerBooking(place.getId());
            spectatorRepository.save(spectator);
            return Optional.of(place);
        }
    }

    @Transactional
    public void cancelBooking(PlaceId placeId) {
        // 1. load the places (IllegalArgumentException if not found)
        // 2. update the place status (AVAILABLE), assigne
        // 3. update the spectator bookings
        // 4. update the concert (make the place available back) + update status
        throw new RuntimeException("implement me !");
    }

}
