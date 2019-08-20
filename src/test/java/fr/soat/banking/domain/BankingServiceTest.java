package fr.soat.banking.domain;

import fr.soat.eventsourcing.impl.InMemoryEventStore;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static fr.soat.banking.domain.AccountStatus.CLOSED;

public class BankingServiceTest {

    AccountRepository repository = new AccountRepository(new InMemoryEventStore());
    BankingService bankingService = new BankingService(repository);

    @Test
    public void should_register_a_new_account_then_use_then_close() {
        AccountId accountId = bankingService.openAccount("toto");

        // When
        bankingService.deposit(accountId, 100);
        bankingService.deposit(accountId, 200);
        bankingService.withdraw(accountId, 300);
        bankingService.closeAccount(accountId);

        // Then
        Account reloadedAccount = repository.load(accountId);
        Assertions.assertThat(reloadedAccount.getStatus()).isEqualTo(CLOSED);
        Assertions.assertThat(reloadedAccount.getBalance()).isEqualTo(0);
    }

    @Test
    public void should_successfully_transfer() {
        AccountId aliceAccountId = bankingService.openAccount("alice");
        AccountId bobAccountId = bankingService.openAccount("bob");

        // When
        bankingService.deposit(aliceAccountId, 200);
        bankingService.transfer(aliceAccountId, bobAccountId, 50);

        // Then
        Account aliceAccount = repository.load(aliceAccountId);
        Account bobAccount = repository.load(bobAccountId);
        Assertions.assertThat(aliceAccount.getBalance()).isEqualTo(150);
        Assertions.assertThat(bobAccount.getBalance()).isEqualTo(50);
    }
}
