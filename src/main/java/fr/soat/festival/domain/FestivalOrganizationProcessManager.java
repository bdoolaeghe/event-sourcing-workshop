package fr.soat.festival.domain;

import fr.soat.festival.domain.concert.ConcertRepository;
import fr.soat.festival.domain.concert.model.Concert;
import fr.soat.festival.domain.concert.model.ConcertOpeningRequested;
import fr.soat.festival.domain.place.PlaceRepository;
import fr.soat.festival.domain.place.model.Place;
import fr.soat.festival.domain.place.model.PlaceId;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class FestivalOrganizationProcessManager {

    ConcertRepository concertRepository;
    PlaceRepository placeRepository;

    @EventListener
    public void on(ConcertOpeningRequested concertOpeningRequested) {
        // creates places
        List<PlaceId> room = IntStream.range(0, concertOpeningRequested.getPlaces())
                .mapToObj(i -> Place.create().allocateTo(concertOpeningRequested.getArtist(), concertOpeningRequested.getPrice()))
                .map(placeRepository::save)
                .map(Place::getId)
                .collect(toList());

        // attach places to new created concert
        Concert concert = concertRepository.load(concertOpeningRequested.getArtist());
        concertRepository.save(concert.assignRoom(room));
    }

}
