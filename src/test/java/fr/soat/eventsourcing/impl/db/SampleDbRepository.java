package fr.soat.eventsourcing.impl.db;

import fr.soat.eventsourcing.api.EventStore;
import org.springframework.stereotype.Repository;

@Repository
public class SampleDbRepository extends AbstractDbRepository<SampleId, SampleEntity, SampleEvent> {

    public SampleDbRepository(EventStore<SampleId, SampleEvent> es) {
        super(es);
    }

    @Override
    protected SampleId newEntityId() {
        return SampleId.from(super.generateEntityId());
    }

    @Override
    protected SampleEntity create(SampleId entityId, int version) {
        return SampleEntity.create(entityId, version);
    }
}
