package fr.soat.banking.domain;

public class BankingService {

    private AccountRepository repository;

    public BankingService(AccountRepository repository) {
        this.repository = repository;
    }

    public AccountId createAccount(String owner) {
        final Account account = Account
                        .create()
                        .register(owner);

        repository.save(account);
        return account.getId();
    }

    public Account loadAccount(AccountId id) {
        return repository.load(id);
    }

    public void deposit(AccountId id, int amount) {
        final Account account = repository.load(id);
        account.deposit(amount);
        repository.save(account);
    }

    public void withdraw(AccountId id, int amount) {
        final Account account = repository.load(id);
        account.withdraw(amount);
        repository.save(account);
    }

    public void closeAccount(AccountId id) {
        final Account account = repository.load(id);
        account.close();
        repository.save(account);
    }
}
