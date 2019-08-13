package fr.soat.banking.domain;

public class BankingService {

//    private AccountRepository accounts;
//
//    public BankingService(AccountRepository accounts) {
//
//        this.accounts = accounts;
//    }
//
//    public AccountId createAccount(String owner) {
//        final Account account = new Account();
//        account.createAccount(owner);
//        accounts.save(account);
//        return account.id();
//    }
//
//    public Account loadAccount(AccountId id) {
//        final Account account = accounts.load(id);
//        if (account.isClosed()) {
//            throw new AccountClosedException(id);
//        }
//        return account;
//    }
//
//    public void deposit(AccountId id, String amount) {
//        final Account account = accounts.load(id);
//        account.deposit(Integer.valueOf(amount));
//        accounts.save(account);
//    }
//
//    public void withdraw(AccountId id, Integer amount) {
//        final Account account = accounts.load(id);
//        account.withdraw(amount);
//        accounts.save(account);
//    }
//
//    public void closeAccount(AccountId id) {
//        final Account account = accounts.load(id);
//        account.close();
//        accounts.save(account);
//    }
}
