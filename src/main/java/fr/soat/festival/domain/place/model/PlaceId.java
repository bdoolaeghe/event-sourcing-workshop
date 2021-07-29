package fr.soat.festival.domain.place.model;

import fr.soat.eventsourcing.api.EntityId;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public final class PlaceId implements EntityId {

    private String id;

    public static PlaceId of(String id) {
        return new PlaceId(id);
    }

    @Override
    public String getIdValue() {
        return id;
    }
}
