package fr.soat.conference.domain;

import fr.soat.conference.domain.order.*;
import org.junit.Test;

import static fr.soat.conference.domain.order.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;

public class AccountTest {

    @Test
    public void should_succeed_a_classic_scenario() {
        // Given
        Order order = Order.create();

        // When
        order.open("toto")
                .deposit(100)
                .deposit(100)
                .withdraw(200)
                .close();

        // Then
        assertThat(order.getOwner()).isEqualTo("toto");
        assertThat(order.getBalance()).isEqualTo(0);
        assertThat(order.getVersion()).isEqualTo(5);
        assertThat(order.getStatus()).isEqualTo(CLOSED);
        assertThat(order.getChanges())
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
        Order order = Order.create();

        // When
        assertThatThrownBy(() -> order.open("toto")
                .deposit(100)
                .withdraw(200))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessage("Withdrawal of 200 can not be applied with balance of 100");
        assertThat(order.getBalance()).isEqualTo(100);
        assertThat(order.getVersion()).isEqualTo(2);
    }

    @Test
    public void should_fail_with_invalid_decisions_on_new_account() {
        // Given
        Order order = Order.create();
        assertThat(order.getStatus()).isEqualTo(NEW);

        // When
        assertThatThrownBy(() -> order.withdraw(100))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> order.deposit(100))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> order.close())
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void should_fail_with_invalid_decisions_on_open_account() {
        // Given
        Order order = Order.create();
        order.open("alice");
        assertThat(order.getStatus()).isEqualTo(OPEN);

        // When
        assertThatThrownBy(() -> order.open("bob"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void should_fail_with_invalid_decisions_on_closed_account() {
        // Given
        Order order = Order.create();
        order.open("alice")
                .close();
        assertThat(order.getStatus()).isEqualTo(CLOSED);

        // When
        assertThatThrownBy(() -> order.open("bob"))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> order.withdraw(100))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> order.deposit(100))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> order.close())
                .isInstanceOf(UnsupportedOperationException.class);
    }

}
