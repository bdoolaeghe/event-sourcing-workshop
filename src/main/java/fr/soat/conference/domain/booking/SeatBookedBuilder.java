package fr.soat.conference.domain.booking;

import fr.soat.conference.domain.order.OrderId;

public final class SeatBookedBuilder {
    private ConferenceName conferenceName;
    private OrderId orderId;
    private Seat seat;

    private SeatBookedBuilder() {
    }

    public static SeatBookedBuilder aSeatBooked() {
        return new SeatBookedBuilder();
    }

    public SeatBookedBuilder conferenceName(ConferenceName conferenceName) {
        this.conferenceName = conferenceName;
        return this;
    }

    public SeatBookedBuilder orderId(OrderId orderId) {
        this.orderId = orderId;
        return this;
    }

    public SeatBookedBuilder seat(Seat seat) {
        this.seat = seat;
        return this;
    }

    public SeatBooked build() {
        return new SeatBooked(conferenceName, orderId, seat);
    }
}
