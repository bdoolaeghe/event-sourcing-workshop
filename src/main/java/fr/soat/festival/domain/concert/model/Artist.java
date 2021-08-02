package fr.soat.festival.domain.concert.model;

import fr.soat.eventsourcing.api.EntityId;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public final class Artist implements EntityId {

    @Getter
    private String name;

    public static Artist named(String name) {
        return new Artist(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getIdValue() {
        return getName();
    }
}
