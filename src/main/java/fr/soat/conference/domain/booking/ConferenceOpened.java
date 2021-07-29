package fr.soat.conference.domain.booking;


import fr.soat.eventsourcing.api.EvolutionFunction;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;
import java.util.stream.IntStream;

import static com.google.common.collect.Lists.newArrayList;
import static fr.soat.conference.domain.booking.ConferenceStatus.OPEN;
import static fr.soat.eventsourcing.api.Event.concat;
import static java.util.stream.Collectors.toList;

@Value
@EqualsAndHashCode(callSuper = true)
public class ConferenceOpened extends ConferenceEvent {

    int places;
    int seatPrice;

    public ConferenceOpened(ConferenceName conferenceName, int places, int seatPrice) {
        super(conferenceName);
        this.places = places;
        this.seatPrice = seatPrice;
    }

    @Override
    @EvolutionFunction
    public Conference applyOn(Conference conference) {
        List<Seat> seats = IntStream.range(1, places + 1)
                .mapToObj(Seat::new)
                .collect(toList());

        return conference.toBuilder()
                .status(OPEN)
                .seatPrice(seatPrice)
                .seats(seats)
                .availableSeats(newArrayList(seats))
                .events(concat(conference.getEvents(), this))
                .build();
    }
}
