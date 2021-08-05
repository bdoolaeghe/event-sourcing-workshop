package fr.soat.festival.domain.spectator;

import fr.soat.festival.domain.spectator.model.Account;
import fr.soat.festival.domain.spectator.model.SpectatorId;

public interface AccountRepository {

    void create(Account account);
    void update(Account account);
    Account getOne(SpectatorId spectatorId);

}
