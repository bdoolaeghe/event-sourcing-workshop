package fr.soat.banking.domain;

import fr.soat.eventsourcing.api.Command;

public class BankingService {

    private AccountRepository repository;

    public BankingService(AccountRepository repository) {
        this.repository = repository;
    }

    @Command
    public AccountId openAccount(String owner) {
        final Account account = Account
                        .create()
                        .open(owner);

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

    @Command
    public void transfer(AccountId idFrom, AccountId idTo, int amount) {
        final Account accountFrom = repository.load(idFrom);
        final Account accountTo = repository.load(idTo);
        accountFrom.requestTransfer(accountTo, amount);
        repository.save(accountFrom);
        repository.save(accountTo);
    }
}
