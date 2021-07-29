package fr.soat.conference.infra.booking;

import fr.soat.conference.domain.booking.Conference;
import fr.soat.conference.domain.booking.ConferenceEvent;
import fr.soat.conference.domain.booking.ConferenceName;
import fr.soat.eventsourcing.api.EventStore;
import fr.soat.eventsourcing.impl.db.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ConferenceRepository extends AbstractRepository<ConferenceName, Conference, ConferenceEvent> {

    public ConferenceRepository(EventStore<ConferenceName, ConferenceEvent> es) {
        super(es);
    }

    @Override
    protected ConferenceName newEntityId() {
        throw new UnsupportedOperationException("Can not generate a conference name");
    }

    @Override
    protected Conference create(ConferenceName conferenceName) {
        return Conference.create(conferenceName);
    }
    //
//    private final EventStore eventStore;
//
//    public ConferenceRepository(EventStore eventStore) {
//        this.eventStore = eventStore;
//    }
//
//    public void save(Conference conference) {
//        ConferenceName aggregateId = conference.getId();
//        eventStore.store(aggregateId, conference.getChanges());
//    }
//
//    public Conference load(ConferenceName conferenceName) {
//        List<ConferenceEvent> events = asRoomEvents(eventStore.loadEvents(conferenceName));
//        return hydrate(conferenceName, events);
//    }
//
//    private static Conference hydrate(ConferenceName conferenceName, List<ConferenceEvent> events) {
//        Conference conference = new Conference(conferenceName, 10);
//        events.forEach(event -> event.applyOn(conference));
//        return conference;
//    }
//
//    private List<ConferenceEvent> asRoomEvents(List<Event> events) {
//        return events.stream().map(event -> (ConferenceEvent) event).collect(toList());
//    }
}
