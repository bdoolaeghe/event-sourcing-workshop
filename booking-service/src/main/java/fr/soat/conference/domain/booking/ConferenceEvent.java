package fr.soat.conference.domain.booking;

import fr.soat.eventsourcing.api.AggregateId;
import fr.soat.eventsourcing.api.Event;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = "seatId")
public abstract class ConferenceEvent implements Event {

    private final ConferenceName conferenceName;

    public ConferenceEvent(ConferenceName conferenceName) {
        this.conferenceName = conferenceName;
    }

    public AggregateId getAggregateId() {
        return conferenceName;
    }

    public abstract void applyOn(Conference conference);

}
