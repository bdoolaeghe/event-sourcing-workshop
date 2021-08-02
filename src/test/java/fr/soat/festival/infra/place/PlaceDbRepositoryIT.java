package fr.soat.festival.infra.place;

import fr.soat.eventsourcing.configuration.DbEventStoreConfiguration;
import fr.soat.festival.application.configuration.FestivalConfiguration;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.place.PlaceRepository;
import fr.soat.festival.domain.place.model.Place;
import fr.soat.festival.domain.place.model.PlaceId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DbEventStoreConfiguration.class,
        FestivalConfiguration.class
})
@Transactional
class PlaceDbRepositoryIT {

    @Autowired
    PlaceRepository placeRepository;

    Artist anArtist = Artist.named("Marcel & son orchestre");

    @Test
    public void should_store_and_reload_place() {
        // Given
        Place place = Place.create().allocateTo(anArtist, 3);
        
        // When
        Place savedPlace = placeRepository.save(place);
        Place reloadedPlace = placeRepository.load(savedPlace.getId());

        // Then
        assertThat(reloadedPlace).isEqualToIgnoringGivenFields(place, "id", "version");
        assertThat(reloadedPlace.getId()).isEqualTo(savedPlace.getId());
    }

    @Test
    void should_bulk_load() {
        // Given
        List<PlaceId> room = Stream.of(
                Place.create().allocateTo(anArtist, 3),
                Place.create().allocateTo(anArtist, 3)
        )
                .map(placeRepository::save)
                .map(Place::getId)
                .collect(toList());

        // When
        List<Place> reloadedPlaces = placeRepository.load(room);

        // Then
        assertThat(reloadedPlaces).hasSize(2);
        assertThat(reloadedPlaces).extracting(Place::getArtist).containsOnly(anArtist);
        assertThat(reloadedPlaces).extracting(Place::getPrice).containsOnly(3);
    }
}
