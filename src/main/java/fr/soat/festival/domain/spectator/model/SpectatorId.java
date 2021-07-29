package fr.soat.festival.domain.spectator.model;

import fr.soat.eventsourcing.api.EntityId;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class SpectatorId implements EntityId {

    private String id;

    @Override
    public String getIdValue() {
        return id;
    }
}
