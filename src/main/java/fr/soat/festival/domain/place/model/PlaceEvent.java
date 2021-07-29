package fr.soat.festival.domain.place.model;


import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.eventsourcing.api.EntityId;
import fr.soat.eventsourcing.api.Event;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface PlaceEvent extends Event<Place> {
}
