package fr.soat.eventsourcing.impl.db;

import fr.soat.eventsourcing.configuration.DbEventStoreConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DbEventStoreConfiguration.class
})
@Transactional
class DBEventStoreIT {

    @Autowired
    DBEventStore<SampleId, SampleEvent> dbEventStore;

    @Test
    void should_create_save_and_reload() {
        // Given
        SampleEntity e = SampleEntity
                .create(SampleId.from("1"), 0)
                .setSampleValue("create this");
        // When
        dbEventStore.store(e.getId(), e.getEvents(), 0);
        List<SampleEvent> reloadedEvents = dbEventStore.loadEvents(e.getId());

        // Then
        assertThat(reloadedEvents).isEqualTo(e.getEvents());
    }

}
