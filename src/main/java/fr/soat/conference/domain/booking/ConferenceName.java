package fr.soat.conference.domain.booking;

import fr.soat.eventsourcing.api.EntityId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Builder
public final class ConferenceName implements EntityId {

    @Getter
    private final String name;

    public static ConferenceName name(String name) {
        return new ConferenceName(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getValue() {
        return getName();
    }
}
