package fr.soat.festival.domain.spectator;

import fr.soat.eventsourcing.impl.db.EventStoreRepository;
import fr.soat.festival.domain.spectator.model.Spectator;
import fr.soat.festival.domain.spectator.model.SpectatorEvent;
import fr.soat.festival.domain.spectator.model.SpectatorId;

public interface SpectatorRepository extends EventStoreRepository<SpectatorId, Spectator, SpectatorEvent> {
}
