package fr.soat.conference.domain.booking;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
@Getter
public class ConferenceOpened extends ConferenceEvent {

    private final int places;
    private final int seatPrice;

    public ConferenceOpened(ConferenceName conferenceName, int palces, int seatPrice) {
        super(conferenceName);
        this.places = palces;
        this.seatPrice = seatPrice;
    }

    @Override
    public void applyOn(Conference conference) {
        conference.apply(this);
    }
}
