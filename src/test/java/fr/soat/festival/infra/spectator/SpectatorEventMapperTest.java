package fr.soat.festival.infra.spectator;

import fr.soat.eventsourcing.impl.db.EventMapper;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.place.model.PlaceId;
import fr.soat.festival.domain.spectator.model.SpectatorBookingRegistered;
import fr.soat.festival.domain.spectator.model.SpectatorBookingRejected;
import fr.soat.festival.domain.spectator.model.SpectatorEvent;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SpectatorEventMapperTest {

    @ParameterizedTest
    @MethodSource("someSpectatorEvents")
    void should_serdes_SpectatorEvent(SpectatorEvent event) {
        // When
        String json = EventMapper.toJson(event);
        SpectatorEvent deserEvent = EventMapper.fromJson(json, event.getClass());

        // Then
        assertThat(deserEvent).isEqualToComparingFieldByField(event);
    }

    private static Stream<SpectatorEvent> someSpectatorEvents() {
        return Stream.of(
                new SpectatorBookingRejected(Artist.named("Marcel & son orchestre")),
                new SpectatorBookingRegistered(PlaceId.from("1"))
        );
    }
}
