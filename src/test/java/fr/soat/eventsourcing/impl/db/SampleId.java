package fr.soat.eventsourcing.impl.db;

import fr.soat.eventsourcing.api.EntityId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class SampleId implements EntityId {

    private String id;

    public static SampleId from(String id) {
        return new SampleId(id);
    }

    @Override
    public String getIdValue() {
        return id;
    }
}
