package fr.soat.banking.domain.account;

import fr.soat.banking.domain.account.model.Account;
import fr.soat.banking.domain.account.model.AccountEvent;
import fr.soat.banking.domain.account.model.AccountNumber;
import fr.soat.eventsourcing.impl.db.EventStoreRepository;

public interface AccountRepository extends EventStoreRepository<AccountNumber, Account, AccountEvent> {
}
