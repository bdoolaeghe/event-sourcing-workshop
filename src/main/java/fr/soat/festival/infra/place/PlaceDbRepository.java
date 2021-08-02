package fr.soat.festival.infra.place;

import fr.soat.eventsourcing.api.EventStore;
import fr.soat.eventsourcing.impl.db.AbstractDbRepository;
import fr.soat.festival.domain.place.PlaceRepository;
import fr.soat.festival.domain.place.model.Place;
import fr.soat.festival.domain.place.model.PlaceEvent;
import fr.soat.festival.domain.place.model.PlaceId;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
public class PlaceDbRepository extends AbstractDbRepository<PlaceId, Place, PlaceEvent>
        implements PlaceRepository {

    public PlaceDbRepository(EventStore<PlaceId, PlaceEvent> es) {
        super(es);
    }

    @Override
    protected PlaceId newEntityId() {
        return PlaceId.from(super.generateEntityId());
    }

    @Override
    protected Place create(PlaceId id, int version) {
        return Place.create(id, version);
    }

    @Override
    public List<Place> load(List<PlaceId> ids) {
        return ids.stream()
                .map(this::load)
                .collect(toList());
    }
}
