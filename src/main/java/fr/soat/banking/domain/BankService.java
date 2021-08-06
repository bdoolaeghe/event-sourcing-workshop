package fr.soat.banking.domain;

import fr.soat.banking.domain.account.AccountRepository;
import fr.soat.banking.domain.account.model.Account;
import fr.soat.banking.domain.account.model.AccountNumber;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BankService {

    private final AccountRepository accountRepository;

    public AccountNumber openAccount(String owner, int initialDeposit) {
        Account account = Account
                .create()
                .register(owner, initialDeposit);

        account = accountRepository.save(account);
        return account.getNumber();
    }

    public Account loadAccount(AccountNumber accountNumber) {
        return accountRepository.load(accountNumber);
    }

    public void deposit(AccountNumber accountNumber, int amount) {
        Account account = accountRepository.load(accountNumber);
        account = account.deposit(amount);
        accountRepository.save(account);
    }

    public void withdraw(AccountNumber accountNumber, int amount) {
        Account account = accountRepository.load(accountNumber);
        account = account.withdraw(amount);
        accountRepository.save(account);
    }

    public void closeAccount(AccountNumber accountNumber) {
        Account account = accountRepository.load(accountNumber);
        account = account.close();
        accountRepository.save(account);
    }
}
