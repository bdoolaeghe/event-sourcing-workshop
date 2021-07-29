package fr.soat.festival.infra.concert;

import fr.soat.eventsourcing.api.EventStore;
import fr.soat.eventsourcing.impl.db.AbstractDbRepository;
import fr.soat.festival.domain.concert.ConcertRepository;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.concert.model.Concert;
import fr.soat.festival.domain.concert.model.ConcertEvent;
import org.springframework.stereotype.Repository;

@Repository
public class ConcertDbRepository extends AbstractDbRepository<Artist, Concert, ConcertEvent> implements ConcertRepository {

    public ConcertDbRepository(EventStore<Artist, ConcertEvent> es) {
        super(es);
    }

    @Override
    protected Artist newEntityId() {
        return Artist.named("Artist " + super.generateEntityId());
    }

    @Override
    protected Concert create(Artist artist) {
        return Concert.create(artist);
    }
}
