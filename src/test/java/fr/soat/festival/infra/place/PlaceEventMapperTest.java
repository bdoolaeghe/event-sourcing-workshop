package fr.soat.festival.infra.place;

import fr.soat.eventsourcing.impl.db.EventMapper;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.place.model.PlaceAssigned;
import fr.soat.festival.domain.place.model.PlaceAssignmentCanceled;
import fr.soat.festival.domain.place.model.PlaceEvent;
import fr.soat.festival.domain.place.model.PlacePricedAndAllocated;
import fr.soat.festival.domain.spectator.model.SpectatorId;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PlaceEventMapperTest {

    @ParameterizedTest
    @MethodSource("somePlaceEvents")
    void should_serdes_PlaceEvent(PlaceEvent event) {
        // When
        String json = EventMapper.toJson(event);
        PlaceEvent deserEvent = EventMapper.fromJson(json, event.getClass());

        // Then
        assertThat(deserEvent).isEqualToComparingFieldByField(event);
    }

    private static Stream<PlaceEvent> somePlaceEvents() {
        return Stream.of(
                new PlacePricedAndAllocated(Artist.named("Marcel & son orchestre"), 12),
                new PlaceAssigned(SpectatorId.from("1")),
                new PlaceAssignmentCanceled()
        );
    }
}
