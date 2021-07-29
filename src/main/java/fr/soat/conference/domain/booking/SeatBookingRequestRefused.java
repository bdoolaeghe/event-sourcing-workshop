package fr.soat.conference.domain.booking;

import fr.soat.conference.domain.order.OrderId;
import fr.soat.eventsourcing.api.EvolutionFunction;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static fr.soat.eventsourcing.api.Event.concat;

@EqualsAndHashCode
@ToString(callSuper = true)
@Getter
public class SeatBookingRequestRefused extends ConferenceEvent {

    private final OrderId orderId;

    public SeatBookingRequestRefused(ConferenceName id, OrderId orderId) {
        super(id);
        this.orderId = orderId;
    }

    @Override
    @EvolutionFunction
    public Conference applyOn(Conference conference) {
        return conference.toBuilder()
                .events(concat(conference.getEvents(), this))
                .build();
    }
}
