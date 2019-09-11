package fr.soat.conference.domain.order;

import fr.soat.eventsourcing.api.AggregateId;
import fr.soat.eventsourcing.api.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString(of = "orderId")
public abstract class OrderEvent implements Event {

    private final OrderId orderId;

    public AggregateId getAggregateId() {
        return orderId;
    }

    public abstract void applyOn(Order order);

}
