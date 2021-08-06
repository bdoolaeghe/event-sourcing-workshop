package fr.soat.banking.domain;

import fr.soat.banking.application.configuration.BankingConfiguration;
import fr.soat.banking.domain.account.AccountRepository;
import fr.soat.banking.domain.account.model.*;
import fr.soat.eventsourcing.configuration.DbEventStoreConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DbEventStoreConfiguration.class,
        BankingConfiguration.class
})
@Transactional
public class BankCommandsIT {

    @Autowired
    private BankCommandHandler bankCommandHandler;
    @Autowired
    private BankService bankService;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    void should_open_an_account() {
        // When
        AccountNumber accountNumber = bankCommandHandler.openAccount("toto", 1000);

        // Then
        Account account = bankService.loadAccount(accountNumber);
        assertThat(account.getStatus()).isEqualTo(Account.Status.OPEN);
        assertThat(account.getOwner()).isEqualTo("toto");
        assertThat(account.getBalance()).isEqualTo(1000);
    }

    @Test
    void should_close_an_open_account() {
        // Given
        AccountNumber accountNumber = bankCommandHandler.openAccount("toto", 1000);

        // When
        bankCommandHandler.closeAccount(accountNumber);

        // Then
        Account account = bankService.loadAccount(accountNumber);
        assertThat(account.getStatus()).isEqualTo(Account.Status.CLOSED);
        assertThat(account.getBalance()).isEqualTo(1000);
    }

    @Test
    void should_fail_to_close_a_not_OPEN_account() {
        // Given
        Account account = accountRepository.save(Account.create());

        // When/Then
        assertThatThrownBy(() ->
                bankCommandHandler.closeAccount(account.getNumber())
        )
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Can not close a NEW account");
    }

    @Test
    void should_fail_to_close_a_CLOSED_account() {
        // Given
        AccountNumber accountNumber = bankCommandHandler.openAccount("toto", 1000);
        bankCommandHandler.closeAccount(accountNumber);

        // When/Then
        assertThatThrownBy(() ->
                bankCommandHandler.closeAccount(accountNumber)
        )
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Can not close a CLOSED account");
    }

    @Test
    void should_increase_balance_after_deposit() {
        // Given
        AccountNumber accountNumber = bankCommandHandler.openAccount("toto", 1000);

        // When
        bankCommandHandler.deposit(accountNumber, 100);

        // Then
        Account reloadedAccount = bankService.loadAccount(accountNumber);
        assertThat(reloadedAccount.getBalance()).isEqualTo(1100);
    }

    @Test
    void should_decrease_balance_after_withdrawn() {
        // Given
        AccountNumber accountNumber = bankCommandHandler.openAccount("toto", 1000);

        // When
        bankCommandHandler.withdraw(accountNumber, 100);

        // Then
        Account reloadedAccount = bankService.loadAccount(accountNumber);
        assertThat(reloadedAccount.getBalance()).isEqualTo(900);
    }

    @Test
    void should_fail_withdrawal_when_funds_are_insufficient() {
        // Given
        AccountNumber accountNumber = bankCommandHandler.openAccount("toto", 1000);

        // When/Then
        assertThatThrownBy(() ->
                bankCommandHandler.withdraw(accountNumber, 1100)
        )
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessage("Withdrawal of 1100 can not be applied with balance of 1000");
    }

    @Test
    public void should_register_account_history() {
        // Given
        AccountNumber accountNumber = bankCommandHandler.openAccount("toto", 1000);

        // When
        bankCommandHandler.deposit(accountNumber, 100);
        bankCommandHandler.deposit(accountNumber, 400);
        bankCommandHandler.withdraw(accountNumber, 300);
        bankCommandHandler.closeAccount(accountNumber);

        // Then
        Account reloadedAccount = bankService.loadAccount(accountNumber);
        assertThat(reloadedAccount.getBalance()).isEqualTo(1200);
        assertThat(reloadedAccount.getEvents()).extracting("class")
                .containsExactly(
                        AccountOpened.class,
                        AccountDeposited.class,
                        AccountDeposited.class,
                        AccountWithdrawn.class,
                        AccountClosed.class
                );
    }

}
