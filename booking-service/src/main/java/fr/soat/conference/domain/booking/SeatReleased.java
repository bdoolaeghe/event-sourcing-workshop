package fr.soat.conference.domain.booking;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
@Getter
public class SeatReleased extends ConferenceEvent {

    private final Seat seat;

    public SeatReleased(ConferenceName id, Seat bookedSeat) {
        super(id);
        this.seat = bookedSeat;
    }

    @Override
    public void applyOn(Conference conference) {
        conference.apply(this);
    }
}
