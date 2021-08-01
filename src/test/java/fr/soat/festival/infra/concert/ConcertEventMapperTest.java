package fr.soat.festival.infra.concert;

import fr.soat.eventsourcing.impl.db.EventMapper;
import fr.soat.festival.domain.concert.model.ConcertEvent;
import fr.soat.festival.domain.concert.model.ConcertPlaceBooked;
import fr.soat.festival.domain.concert.model.ConcertPlaceBookingCancelled;
import fr.soat.festival.domain.concert.model.ConcertRoomAssigned;
import fr.soat.festival.domain.place.model.PlaceId;
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
                new ConcertRoomAssigned(asList(
                        PlaceId.from("1"),
                        PlaceId.from("2"))),
                new ConcertPlaceBooked(PlaceId.from("1")),
                new ConcertPlaceBookingCancelled(PlaceId.from("1"))
        );
    }
}
