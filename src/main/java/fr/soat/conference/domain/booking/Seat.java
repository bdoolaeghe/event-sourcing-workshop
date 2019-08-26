package fr.soat.conference.domain.booking;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Seat {

    @Getter
    private final int idd;

    public Seat(int id) {
        this.idd = id;
    }
}
