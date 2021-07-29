package fr.soat.festival.domain.place;

import fr.soat.eventsourcing.impl.db.EventStoreRepository;
import fr.soat.festival.domain.concert.model.Artist;
import fr.soat.festival.domain.concert.model.Concert;
import fr.soat.festival.domain.concert.model.ConcertEvent;
import fr.soat.festival.domain.place.model.Place;
import fr.soat.festival.domain.place.model.PlaceEvent;
import fr.soat.festival.domain.place.model.PlaceId;

import java.util.List;

public interface PlaceRepository extends EventStoreRepository<PlaceId, Place, PlaceEvent> {

    List<Place> load(List<PlaceId> ids);

}
