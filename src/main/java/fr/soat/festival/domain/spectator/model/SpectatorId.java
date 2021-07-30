package fr.soat.festival.domain.spectator.model;

import fr.soat.eventsourcing.api.EntityId;
import fr.soat.festival.domain.place.model.PlaceId;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class SpectatorId implements EntityId {

    private String id;

    public static SpectatorId from(String id) {
        return new SpectatorId(id);
    }

    @Override
    public String getIdValue() {
        return id;
    }
}
