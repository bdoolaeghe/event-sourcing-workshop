package fr.soat.conference.domain.booking;


import com.google.common.collect.Lists;
import fr.soat.eventsourcing.api.EvolutionFunction;
import lombok.ToString;
import lombok.Value;

import java.util.List;

import static fr.soat.conference.domain.booking.ConferenceStatus.OPEN;
import static fr.soat.eventsourcing.api.Event.concat;


@Value
@ToString(callSuper = true)
public class SeatReleased extends ConferenceEvent {

    Seat seat;

    public SeatReleased(ConferenceName conferenceName, Seat bookedSeat) {
        super(conferenceName);
        this.seat = bookedSeat;
    }

    @Override
    @EvolutionFunction
    public Conference applyOn(Conference conference) {
        List<Seat> newAvailablerSeats = Lists.newArrayList(conference.getAvailableSeats());
        newAvailablerSeats.add(seat);

        return conference.toBuilder()
                .status(OPEN)
                .availableSeats(newAvailablerSeats)
                .events(concat(conference.getEvents(), this))
                .build();
    }
}
