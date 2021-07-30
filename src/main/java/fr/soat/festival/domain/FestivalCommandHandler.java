package fr.soat.festival.domain;

import fr.soat.festival.domain.concert.ConcertRepository;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.concert.model.Concert;
import fr.soat.festival.domain.spectator.model.Spectator;
import fr.soat.festival.domain.place.model.Place;
import fr.soat.festival.domain.place.model.PlaceId;
import fr.soat.festival.infra.place.PlaceDbRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class FestivalCommandHandler {

    private final ConcertRepository concertRepository;
    private final PlaceDbRepository placeRepository;

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
    public Place book(Artist artist, Spectator spectator) {
        throw new RuntimeException("implement me !");
        // lookup an available place
        // if not full
        // 1. update the place as booked
        // 2. update the concert with available places (remove booked one)
        // 3. update the spact&tor adding the booked place id
        // if full
        // 1. update the concert with booking attempt failure
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
