package fr.soat.conference.domain.booking;

import fr.soat.conference.domain.order.OrderId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
@Getter
public class SeatsExhausted extends ConferenceEvent {

    private final OrderId orderId;

    public SeatsExhausted(ConferenceName id, OrderId orderId) {
        super(id);
        this.orderId = orderId;
    }

    @Override
    public void applyOn(Conference conference) {
        conference.apply(this);
    }
}