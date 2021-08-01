package fr.soat.festival.infra.spectator;

import fr.soat.eventsourcing.configuration.DbEventStoreConfiguration;
import fr.soat.festival.application.configuration.FestivalConfiguration;
import fr.soat.festival.domain.spectator.SpectatorRepository;
import fr.soat.festival.domain.spectator.model.Spectator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DbEventStoreConfiguration.class,
        FestivalConfiguration.class
})
@Transactional
class SpectatorDbReposIT {

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
