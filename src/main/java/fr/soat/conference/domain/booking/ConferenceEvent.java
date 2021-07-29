package fr.soat.conference.domain.booking;

import fr.soat.eventsourcing.api.EntityId;
import fr.soat.eventsourcing.api.Event;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public abstract class ConferenceEvent implements Event<Conference> {

    private ConferenceName conferenceName;

    public ConferenceEvent(ConferenceName conferenceName) {
        this.conferenceName = conferenceName;
    }

    public EntityId getEntityId() {
        return conferenceName;
    }

}
