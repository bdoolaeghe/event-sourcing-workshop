package fr.soat.festival.domain.dashboard.model;

import fr.soat.festival.domain.concert.model.Artist;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@AllArgsConstructor
@ToString
public class ConcertDashboard {
    Artist artist;
    String bookingIncomesTotal;
    String bookingRatio;
}
