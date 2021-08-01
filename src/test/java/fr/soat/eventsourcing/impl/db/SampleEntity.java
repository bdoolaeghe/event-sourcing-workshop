package fr.soat.eventsourcing.impl.db;

import fr.soat.eventsourcing.api.Entity;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Value
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class SampleEntity implements Entity<SampleId, SampleEvent> {

    SampleId id;
    String sampleValue;

    List<SampleEvent> events;

    public static SampleEntity create() {
        return create(null);
    }

    public static SampleEntity create(SampleId id) {
        return new SampleEntity(
                id,
                null,
                emptyList()
        );
    }

    public List<SampleEvent> getEvents() {
        return events;
    }

    public SampleEntity setSampleValue(String newValue) {
        return new SampleValued(newValue).applyOn(this);
    }
}
