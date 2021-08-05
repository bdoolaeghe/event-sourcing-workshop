package fr.soat.festival.domain;

import fr.soat.festival.domain.concert.ConcertRepository;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.concert.model.Concert;
import fr.soat.festival.domain.place.PlaceRepository;
import fr.soat.festival.domain.place.model.Place;
import fr.soat.festival.domain.place.model.PlaceId;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class FestivalOrganizationService {

    private final ConcertRepository concertRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    public void openConcert(Artist artist, int places, int price) {
        // create concert
        Concert concert = concertRepository.save(Concert.create(artist));

        // creates places
        List<PlaceId> room = IntStream.range(0, places)
                .mapToObj(i -> Place.create().allocateTo(artist, price))
                .map(placeRepository::save)
                .map(Place::getId)
                .collect(toList());

        // then attach to new created concert
        concertRepository.save(concert.assignRoom(room));
    }

}
