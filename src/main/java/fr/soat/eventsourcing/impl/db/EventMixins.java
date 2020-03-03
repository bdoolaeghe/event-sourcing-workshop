package fr.soat.eventsourcing.impl.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.soat.conference.domain.booking.*;
import fr.soat.conference.domain.order.OrderId;

public class EventMixins {

    public static void registerOn(ObjectMapper objectMapper) {
        objectMapper.addMixIn(ConferenceName.class, ConferenceNameMixin.class);
        objectMapper.addMixIn(ConferenceOpened.class, ConferenceOpenedMixin.class);
        objectMapper.addMixIn(SeatBooked.class, SeatBookedMixin.class);
        objectMapper.addMixIn(SeatReleased.class, SeatReleasedMixin.class);
        objectMapper.addMixIn(OrderId.class, OrderIdMixin.class);
        objectMapper.addMixIn(Seat.class, SeatMixin.class);

    }

    @JsonDeserialize(builder = ConferenceName.ConferenceNameBuilder.class)
    abstract class ConferenceNameMixin {}
    @JsonDeserialize(builder = ConferenceOpenedBuilder.class)
    abstract class ConferenceOpenedMixin {}
    @JsonDeserialize(builder = SeatBookedBuilder.class)
    abstract class SeatBookedMixin {}
    @JsonDeserialize(builder = SeatReleasedBuilder.class)
    abstract class SeatReleasedMixin {}
    @JsonDeserialize(builder = OrderId.OrderIdBuilder.class)
    abstract class OrderIdMixin {}
    @JsonDeserialize(builder = Seat.SeatBuilder.class)
    abstract class SeatMixin {}

}
