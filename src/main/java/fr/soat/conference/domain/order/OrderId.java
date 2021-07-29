package fr.soat.conference.domain.order;

import fr.soat.eventsourcing.api.EntityId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Builder
public final class OrderId implements EntityId {

    @Getter
    private final String value;

    public static OrderId from(String id) {
        return new OrderId(id);
    }

    @Override
    public String toString() {
        return "orderId=" + getValue();
    }
}
