package fr.soat.banking.infra.account;

import fr.soat.banking.domain.account.AccountRepository;
import fr.soat.banking.domain.account.model.Account;
import fr.soat.banking.domain.account.model.AccountEvent;
import fr.soat.banking.domain.account.model.AccountNumber;
import fr.soat.eventsourcing.api.EventStore;
import fr.soat.eventsourcing.impl.db.AbstractDbRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDbRepository extends AbstractDbRepository<AccountNumber, Account, AccountEvent> implements AccountRepository {

    public AccountDbRepository(EventStore<AccountNumber, AccountEvent> es) {
        super(es);
    }

    @Override
    protected AccountNumber newEntityId() {
        return AccountNumber.from("11-1234- " + super.generateEntityId());
    }

    @Override
    protected Account create(AccountNumber accountNumber, int version) {
        return Account.create(accountNumber, version);
    }
}
