package fr.soat.banking.domain;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static fr.soat.banking.domain.AccountStatus.*;
import static org.assertj.core.api.Assertions.*;

public class AccountTest {

    @Test
    public void should_succeed_a_classic_FSM_scenario() {
        // Given
        Account account = Account.create(AccountId.from("1"));

        // When
        account.open("toto", Currency.USD)
                .deposit(Amount.of(100))
                .deposit(Amount.of(100))
                .withdraw(Amount.of(200))
                .close();

        // Then
        Assertions.assertThat(account.getOwner()).isEqualTo("toto");
        Assertions.assertThat(account.getBalance()).isEqualTo(Amount.of(0));
        Assertions.assertThat(account.getVersion()).isEqualTo(5);
        Assertions.assertThat(account.getStatus()).isEqualTo(CLOSED);
        Assertions.assertThat(account.getCurrency()).isEqualTo(Currency.USD);
        Assertions.assertThat(account.getChanges())
                .extracting(event -> tuple(event.getClass()))
                .containsExactly(
                        tuple(AccountOpened.class),
                        tuple(AccountDeposited.class),
                        tuple(AccountDeposited.class),
                        tuple(AccountWithdrawn.class),
                        tuple(AccountClosed.class)
                );

    }

    @Test
    public void should_fail_to_get_negative_balance() {
        // Given
        Account account = Account.create(AccountId.from("1"));

        // When
        assertThatThrownBy(() -> account.open("toto", Currency.EUR)
                .deposit(Amount.of(100))
                .withdraw(Amount.of(200)))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessage("Withdrawal of 200.0 can not be applied with balance of 100.0");
        assertThat(account.getBalance()).isEqualTo(Amount.of(100));
        assertThat(account.getVersion()).isEqualTo(2);
    }

    @Test
    public void should_fail_with_invalid_decisions_on_new_account() {
        // Given
        Account account = Account.create(AccountId.from("1"));
        assertThat(account.getStatus()).isEqualTo(NEW);

        // When
        assertThatThrownBy(() -> account.withdraw(Amount.of(100)))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> account.deposit(Amount.of(100)))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> account.close())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void should_fail_with_invalid_decisions_on_open_account() {
        // Given
        Account account = Account.create(AccountId.from("1"));
        account.open("alice", Currency.EUR);
        assertThat(account.getStatus()).isEqualTo(OPEN);

        // When
        assertThatThrownBy(() -> account.open("bob", Currency.EUR))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void should_fail_with_invalid_decisions_on_closed_account() {
        // Given
        Account account = Account.create(AccountId.from("1"));
        account.open("alice", Currency.EUR)
                .close();
        assertThat(account.getStatus()).isEqualTo(CLOSED);

        // When
        assertThatThrownBy(() -> account.open("bob", Currency.EUR))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> account.withdraw(Amount.of(100)))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> account.deposit(Amount.of(100)))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> account.close())
                .isInstanceOf(UnsupportedOperationException.class);    }
}
