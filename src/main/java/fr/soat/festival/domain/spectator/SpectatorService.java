package fr.soat.festival.domain.spectator;

import fr.soat.festival.domain.spectator.model.Account;
import fr.soat.festival.domain.spectator.model.Spectator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SpectatorService {

    SpectatorRepository spectatorRepository;
    AccountRepository accountRepository;

    @Transactional
    public Spectator createWithAccount(int balance) {
        Spectator spectator = spectatorRepository.save(Spectator.create());
        accountRepository.create(new Account(spectator.getId().getIdValue(), balance));
        return spectator;
    }
}
