package fr.soat.festival.domain.concert;

import fr.soat.eventsourcing.impl.db.EventStoreRepository;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.concert.model.Concert;
import fr.soat.festival.domain.concert.model.ConcertEvent;

public interface ConcertRepository extends EventStoreRepository<Artist, Concert, ConcertEvent> {
}
