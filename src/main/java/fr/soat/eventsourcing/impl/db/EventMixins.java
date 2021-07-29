package fr.soat.eventsourcing.impl.db;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EventMixins {

    public static void registerOn(ObjectMapper objectMapper) {
//        objectMapper.addMixIn(Artist.class, ConferenceNameMixin.class);
//        objectMapper.addMixIn(ConcertOpened.class, ConferenceOpenedMixin.class);
//        objectMapper.addMixIn(SeatBooked.class, SeatBookedMixin.class);
//        objectMapper.addMixIn(SeatReleased.class, SeatReleasedMixin.class);
//        objectMapper.addMixIn(OrderId.class, OrderIdMixin.class);
//        objectMapper.addMixIn(Place.class, SeatMixin.class);

    }

//    @JsonDeserialize(builder = Artist.ConferenceNameBuilder.class)
//    abstract class ConferenceNameMixin {}
//    @JsonDeserialize(builder = ConferenceOpenedBuilder.class)
//    abstract class ConferenceOpenedMixin {}
//    @JsonDeserialize(builder = SeatBookedBuilder.class)
//    abstract class SeatBookedMixin {}
//    @JsonDeserialize(builder = SeatReleasedBuilder.class)
//    abstract class SeatReleasedMixin {}
//    @JsonDeserialize(builder = OrderId.OrderIdBuilder.class)
//    abstract class OrderIdMixin {}
//    @JsonDeserialize(builder = Place.SeatBuilder.class)
//    abstract class SeatMixin {}

}
