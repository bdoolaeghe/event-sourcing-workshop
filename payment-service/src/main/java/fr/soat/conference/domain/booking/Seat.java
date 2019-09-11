package fr.soat.conference.domain.booking;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Seat {

    @Getter
    private final ConferenceName conference;
    @Getter
    private final int placeNumber;

    public Seat(ConferenceName conference, int placeNumber) {
        this.conference = conference;
        this.placeNumber = placeNumber;
    }
}
