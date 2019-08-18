package fr.soat.banking.domain;

import fr.soat.eventsourcing.api.Command;

public class BankingService {

    private AccountRepository repository;

    public BankingService(AccountRepository repository) {
        this.repository = repository;
    }

    @Command
    public AccountId openAccount(String owner, Currency currency) {
        final Account account = Account
                        .create(repository.nextId())
                        .open(owner, currency);

        repository.save(account);
        return account.getId();
    }

    public Account loadAccount(AccountId id) {
        return repository.load(id);
    }

    @Command
    public void deposit(AccountId id, int amount) {
        final Account account = repository.load(id);
        account.deposit(amount);
        repository.save(account);
    }

    @Command
    public void withdraw(AccountId id, int amount) {
        final Account account = repository.load(id);
        account.withdraw(amount);
        repository.save(account);
    }

    @Command
    public void closeAccount(AccountId id) {
        final Account account = repository.load(id);
        account.close();
        repository.save(account);
    }
}
