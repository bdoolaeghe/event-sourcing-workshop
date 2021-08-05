package fr.soat.festival.domain;

import fr.soat.festival.domain.concert.ConcertRepository;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.concert.model.Concert;
import fr.soat.festival.domain.place.PlaceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class FestivalOrganizationService {

    private final ConcertRepository concertRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    public void openConcert(Artist artist, int places, int price) {
        Concert concert = Concert
                .create(artist)
                .open(places, price);
        concertRepository.save(concert);
    }

}
