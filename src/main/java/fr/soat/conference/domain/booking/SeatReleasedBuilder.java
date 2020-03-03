package fr.soat.conference.domain.booking;

public final class SeatReleasedBuilder {
    private Seat seat;
    private ConferenceName conferenceName;

    private SeatReleasedBuilder() {
    }

    public static SeatReleasedBuilder aSeatReleased() {
        return new SeatReleasedBuilder();
    }

    public SeatReleasedBuilder seat(Seat seat) {
        this.seat = seat;
        return this;
    }

    public SeatReleasedBuilder conferenceName(ConferenceName conferenceName) {
        this.conferenceName = conferenceName;
        return this;
    }

    public SeatReleased build() {
        return new SeatReleased(conferenceName, seat);
    }
}
