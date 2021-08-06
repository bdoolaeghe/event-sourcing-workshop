package fr.soat.banking.domain;

import fr.soat.banking.domain.account.model.AccountNumber;
import fr.soat.eventsourcing.api.Command;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BankCommandHandler {

    private final BankService bankService;

    @Command
    public AccountNumber openAccount(String owner, int initialDeposit) {
        return bankService.openAccount(owner, initialDeposit);
    }

    @Command
    public void deposit(AccountNumber accountNumber, int amount) {
        bankService.deposit(accountNumber, amount);
    }

    @Command
    public void withdraw(AccountNumber accountNumber, int amount) {
        bankService.withdraw(accountNumber, amount);
    }

    @Command
    public void closeAccount(AccountNumber accountNumber) {
        bankService.closeAccount(accountNumber);
    }
}
