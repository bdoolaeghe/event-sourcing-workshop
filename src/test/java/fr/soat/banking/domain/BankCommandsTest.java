package fr.soat.banking.domain;

import fr.soat.banking.application.configuration.BankConfig;
import fr.soat.eventsourcing.impl.InMemoryEventStore;
import org.assertj.core.api.Assertions;
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
public class BankCommandsTest {

    @Autowired
    AccountRepository repository;
    @Autowired
    BankCommandHandler bankCommandHandler;

    @Test
    public void should_register_a_new_account_then_use_then_close() {
        AccountId accountId = bankCommandHandler.openAccount("toto");

        // When
        bankCommandHandler.deposit(accountId, 100);
        bankCommandHandler.deposit(accountId, 200);
        bankCommandHandler.withdraw(accountId, 300);
        bankCommandHandler.closeAccount(accountId);

        // Then
        Account reloadedAccount = bankCommandHandler.loadAccount(accountId);
        Assertions.assertThat(reloadedAccount.getStatus()).isEqualTo(CLOSED);
        Assertions.assertThat(reloadedAccount.getBalance()).isEqualTo(0);
    }


    @Test
    public void should_successfully_transfer() {
        AccountId aliceAccountId = bankCommandHandler.openAccount("alice");
        bankCommandHandler.deposit(aliceAccountId, 200);
        AccountId bobAccountId = bankCommandHandler.openAccount("bob");

        // When
        bankCommandHandler.transfer(aliceAccountId, bobAccountId, 50);

        // Then
        Account aliceAccount = bankCommandHandler.loadAccount(aliceAccountId);
        Account bobAccount = bankCommandHandler.loadAccount(bobAccountId);
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
        AccountId aliceAccountId = bankCommandHandler.openAccount("alice");
        bankCommandHandler.deposit(aliceAccountId, 200);
        AccountId bobAccountId = bankCommandHandler.openAccount("bob");
        bankCommandHandler.closeAccount(bobAccountId);

        // When
        bankCommandHandler.transfer(aliceAccountId, bobAccountId, 50);

        // Then
        Account aliceAccount = bankCommandHandler.loadAccount(aliceAccountId);
        Account bobAccount = bankCommandHandler.loadAccount(bobAccountId);
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
        AccountId aliceAccountId = bankCommandHandler.openAccount("alice");
        bankCommandHandler.deposit(aliceAccountId, 200);
        AccountId bobAccountId = bankCommandHandler.openAccount("bob");

        // When
        bankCommandHandler.transfer(aliceAccountId, bobAccountId, 250);

        // Then
        Account aliceAccount = bankCommandHandler.loadAccount(aliceAccountId);
        Account bobAccount = bankCommandHandler.loadAccount(bobAccountId);
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
