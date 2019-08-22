package fr.soat.banking.domain;

import fr.soat.banking.application.configuration.BankConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static fr.soat.banking.domain.AccountStatus.CLOSED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BankConfig.class)
public class BankingServiceTest {

    @Autowired
    AccountRepository repository;
    @Autowired
    BankingService bankingService;

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
        assertThat(reloadedAccount.getStatus()).isEqualTo(CLOSED);
        assertThat(reloadedAccount.getBalance()).isEqualTo(0);
    }

    @Test
    public void should_successfully_transfer() {
        AccountId aliceAccountId = bankingService.openAccount("alice");
        bankingService.deposit(aliceAccountId, 200);
        AccountId bobAccountId = bankingService.openAccount("bob");

        // When
        bankingService.transfer(aliceAccountId, bobAccountId, 50);

        // Then
        Account aliceAccount = repository.load(aliceAccountId);
        Account bobAccount = repository.load(bobAccountId);
        assertThat(aliceAccount.getBalance()).isEqualTo(150);
        assertThat(aliceAccount.getChanges())
                .extracting(event -> tuple(event.getClass()))
                .containsExactly(
                        tuple(AccountOpened.class),
                        tuple(AccountDeposited.class),
                        tuple(TransferRequested.class),
                        tuple(TransferSent.class)
                );
        assertThat(bobAccount.getBalance()).isEqualTo(50);
        assertThat(bobAccount.getChanges())
                .extracting(event -> tuple(event.getClass()))
                .containsExactly(
                        tuple(AccountOpened.class),
                        tuple(TransferReceived.class)
                );
    }

    @Test
    public void should_fail_transfer_to_closed_account() {
        AccountId aliceAccountId = bankingService.openAccount("alice");
        bankingService.deposit(aliceAccountId, 200);
        AccountId bobAccountId = bankingService.openAccount("bob");
        bankingService.closeAccount(bobAccountId);

        // When
        bankingService.transfer(aliceAccountId, bobAccountId, 50);

        // Then
        Account aliceAccount = repository.load(aliceAccountId);
        Account bobAccount = repository.load(bobAccountId);
        assertThat(aliceAccount.getBalance()).isEqualTo(200);
        assertThat(aliceAccount.getChanges())
                .extracting(event -> tuple(event.getClass()))
                .containsExactly(
                        tuple(AccountOpened.class),
                        tuple(AccountDeposited.class),
                        tuple(TransferRequested.class),
                        tuple(TransferRefused.class)
                );
        assertThat(bobAccount.getBalance()).isEqualTo(0);
        assertThat(bobAccount.getChanges())
                .extracting(event -> tuple(event.getClass()))
                .containsExactly(
                        tuple(AccountOpened.class),
                        tuple(AccountClosed.class)
                );
    }

    @Test
    public void should_fail_transfer_when_funds_are_insufficient() {
        AccountId aliceAccountId = bankingService.openAccount("alice");
        bankingService.deposit(aliceAccountId, 200);
        AccountId bobAccountId = bankingService.openAccount("bob");

        // When
        bankingService.transfer(aliceAccountId, bobAccountId, 250);

        // Then
        Account aliceAccount = repository.load(aliceAccountId);
        Account bobAccount = repository.load(bobAccountId);
        assertThat(aliceAccount.getBalance()).isEqualTo(200);
        assertThat(aliceAccount.getChanges())
                .extracting(event -> tuple(event.getClass()))
                .containsExactly(
                        tuple(AccountOpened.class),
                        tuple(AccountDeposited.class),
                        tuple(TransferRequested.class),
                        tuple(TransferRefused.class)
                );
        assertThat(bobAccount.getBalance()).isEqualTo(0);
        assertThat(bobAccount.getChanges())
                .extracting(event -> tuple(event.getClass()))
                .containsExactly(
                        tuple(AccountOpened.class)
                );
    }
}
