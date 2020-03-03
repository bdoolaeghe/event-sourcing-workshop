package fr.soat.conference.domain.booking;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Builder
public class Seat {

    @Getter
    private final int placeNumber;

    public Seat(int placeNumber) {
        this.placeNumber = placeNumber;
    }
}
