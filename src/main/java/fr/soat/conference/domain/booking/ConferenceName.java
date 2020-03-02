package fr.soat.conference.domain.booking;

import fr.soat.eventsourcing.api.AggregateId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
public final class ConferenceName implements AggregateId {

    @Getter
    private final String name;

    public static ConferenceName next() {
        return new ConferenceName(String.valueOf(AggregateId.idGenerator.getAndIncrement()));
    }

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
