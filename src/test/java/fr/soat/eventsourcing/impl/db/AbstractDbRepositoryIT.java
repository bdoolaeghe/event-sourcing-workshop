package fr.soat.eventsourcing.impl.db;

import fr.soat.eventsourcing.api.EventConcurrentUpdateException;
import fr.soat.eventsourcing.configuration.DbEventStoreConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DbEventStoreConfiguration.class
})
@Transactional
class AbstractDbRepositoryIT {

    @Autowired
    SampleDbRepository repository;

    @Test
    void should_save_and_reload_an_empty_entity() {
        // Given
        SampleEntity e = SampleEntity.create();

        // When
        SampleEntity savedEntity = repository.save(e);
        SampleEntity reloadedEntity = repository.load(savedEntity.getId());

        assertThat(reloadedEntity).isEqualToIgnoringGivenFields(e, "id");
        assertThat(reloadedEntity.getId()).isNotNull();
        assertThat(reloadedEntity.getEvents()).isEmpty();
    }

    @Test
    void should_fail_to_load_inexisting_entity() {
        assertThatThrownBy(() -> repository.load(SampleId.from("inexisting id")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void should_create_amend_save_and_reload() {
        // Given
        SampleEntity e = SampleEntity.create()
                .setSampleValue("a sample value")
                .setSampleValue("another sample value");

        // When
        SampleId id = repository.save(e).getId();
        SampleEntity reloadedEntity = repository.load(id);

        // Then
        assertThat(reloadedEntity.getId()).isEqualTo(id);
        assertThat(reloadedEntity.getSampleValue()).isEqualTo("another sample value");
        assertThat(reloadedEntity.getEvents())
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(Arrays.asList(
                        new SampleValued("a sample value"),
                        new SampleValued("another sample value")
                ));
    }

    @Test
    void should_load_amend_and_update() {
        // Given
        SampleEntity e = SampleEntity.create()
                .setSampleValue("a sample value");
        SampleId id = repository.save(e).getId();
        e = repository.load(id);

        // When
        e = e.setSampleValue("another sample value");
        repository.save(e);
        e = repository.load(id);

        // Then
        assertThat(e.getSampleValue()).isEqualTo("another sample value");
    }

    @Test
    void should_fail_concurrent_update() {
        // Given
        SampleEntity e = SampleEntity.create()
                .setSampleValue("a sample value");
        SampleId id = repository.save(e).getId();
        SampleEntity eV1 = repository.load(id)
                .setSampleValue("a version value");
        SampleEntity eV2 = repository.load(id);
        repository.save(eV1);

        // When/Then
        assertThatThrownBy(() ->
                repository.save(
                        eV2.setSampleValue("another version value")
                ))
                .isInstanceOf(EventConcurrentUpdateException.class)
                .hasMessageContaining("Can not save this concurrent version. Refresh before update and try again.");
    }

}
