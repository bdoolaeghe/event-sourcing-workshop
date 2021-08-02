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
    int version;

    public static SampleEntity create() {
        return create(null, 0);
    }

    public static SampleEntity create(SampleId id, int version) {
        return new SampleEntity(
                id,
                null,
                emptyList(),
                version
        );
    }

    public List<SampleEvent> getEvents() {
        return events;
    }

    public SampleEntity setSampleValue(String newValue) {
        return new SampleValued(newValue).applyOn(this);
    }
}
