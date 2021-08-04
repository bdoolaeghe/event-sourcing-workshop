package fr.soat.festival.infra.concert;

import fr.soat.eventsourcing.impl.db.EventMapper;
import fr.soat.festival.domain.concert.model.*;
import fr.soat.festival.domain.place.model.PlaceId;
import fr.soat.festival.domain.spectator.model.SpectatorId;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class ConcertEventMapperTest {

    @ParameterizedTest
    @MethodSource("someConcertEvents")
    void should_serdes_ConcertEvent(ConcertEvent event) {
        // When
        String json = EventMapper.toJson(event);
        ConcertEvent deserEvent = EventMapper.fromJson(json, event.getClass());

        // Then
        assertThat(deserEvent).isEqualToComparingFieldByField(event);
    }

    private static Stream<ConcertEvent> someConcertEvents() {
        return Stream.of(
                new ConcertOpened(asList(
                        PlaceId.from("1"),
                        PlaceId.from("2"))),
                new ConcertOpeningRequested(Artist.named("Toto"), 3, 4),
                new ConcertOpened(asList(PlaceId.from("1"), PlaceId.from("2"))),
                new ConcertPlaceBookingRequestRejected(Artist.named("Toto"), SpectatorId.from("1")),
                new ConcertPlaceBooked(Artist.named("Toto"), SpectatorId.from("4"), PlaceId.from("1")),
                new ConcertPlaceBookingCanceled(PlaceId.from("1"))
        );
    }
}
