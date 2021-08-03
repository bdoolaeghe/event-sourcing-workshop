package fr.soat.festival.domain.spectator.model;

import fr.soat.eventsourcing.api.EntityId;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
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
