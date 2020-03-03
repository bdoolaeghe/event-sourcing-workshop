package fr.soat.conference.domain.booking;

public final class ConferenceOpenedBuilder {

    private int places;
    private int seatPrice;
    private ConferenceName conferenceName;

    private ConferenceOpenedBuilder() {
    }

    public static ConferenceOpenedBuilder aConferenceOpened() {
        return new ConferenceOpenedBuilder();
    }

    public ConferenceOpenedBuilder places(int places) {
        this.places = places;
        return this;
    }

    public ConferenceOpenedBuilder seatPrice(int seatPrice) {
        this.seatPrice = seatPrice;
        return this;
    }

    public ConferenceOpenedBuilder conferenceName(ConferenceName conferenceName) {
        this.conferenceName = conferenceName;
        return this;
    }

    public ConferenceOpened build() {
        return new ConferenceOpened(conferenceName, places, seatPrice);
    }
}
