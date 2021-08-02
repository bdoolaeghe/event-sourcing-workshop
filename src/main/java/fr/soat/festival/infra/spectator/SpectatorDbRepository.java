package fr.soat.festival.infra.spectator;

import fr.soat.eventsourcing.api.EventStore;
import fr.soat.eventsourcing.impl.db.AbstractDbRepository;
import fr.soat.festival.domain.spectator.SpectatorRepository;
import fr.soat.festival.domain.spectator.model.Spectator;
import fr.soat.festival.domain.spectator.model.SpectatorEvent;
import fr.soat.festival.domain.spectator.model.SpectatorId;
import org.springframework.stereotype.Repository;

@Repository
public class SpectatorDbRepository extends AbstractDbRepository<SpectatorId, Spectator, SpectatorEvent>
        implements SpectatorRepository {

    public SpectatorDbRepository(EventStore<SpectatorId, SpectatorEvent> es) {
        super(es);
    }

    @Override
    protected SpectatorId newEntityId() {
        return SpectatorId.from(super.generateEntityId());
    }

    @Override
    protected Spectator create(SpectatorId id, int version) {
        return Spectator.create(id, version);
    }

}
