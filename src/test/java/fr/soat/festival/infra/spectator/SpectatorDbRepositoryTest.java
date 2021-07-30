package fr.soat.festival.infra.spectator;

import fr.soat.eventsourcing.configuration.DbEventStoreConfiguration;
import fr.soat.festival.application.configuration.ConferenceConfiguration;
import fr.soat.festival.domain.spectator.SpectatorRepository;
import fr.soat.festival.domain.spectator.model.Spectator;
import fr.soat.festival.domain.place.model.PlaceId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DbEventStoreConfiguration.class,
        ConferenceConfiguration.class
})
@Transactional
class SpectatorDbRepositoryTest {

    @Autowired
    SpectatorRepository spectatorRepository;

    @Test
    public void should_store_and_reload_spectator() {
        // Given
        Spectator spectator = Spectator.create();

        // When
        Spectator savedSpectator = spectatorRepository.save(spectator);
        Spectator reloadedSpectator = spectatorRepository.load(savedSpectator.getId());

        // Then
        assertThat(reloadedSpectator).isEqualToIgnoringGivenFields(spectator, "id");
        assertThat(reloadedSpectator.getId()).isEqualTo(savedSpectator.getId());
    }


}
