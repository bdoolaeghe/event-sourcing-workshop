package fr.soat.festival.domain.concert.model;

import lombok.*;

import static fr.soat.util.Util.append;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ConcertOpeningRequested implements ConcertEvent {

    private Artist artist;
    private int places;
    private int price;

    @Override
    public Concert applyOn(Concert concert) {
        return concert.toBuilder()
                .status(Concert.Status.BOOKABLE)
                .events(append(concert.getEvents(), this))
                .build();
    }

}
