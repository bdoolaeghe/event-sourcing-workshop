package fr.soat.conference.domain;

import fr.soat.conference.application.configuration.ConferenceManagementConfig;
import fr.soat.conference.domain.order.*;
import fr.soat.conference.infra.order.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static fr.soat.conference.domain.order.OrderStatus.CLOSED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ConferenceManagementConfig.class)
public class BankingServiceTest {

    @Autowired
    OrderRepository repository;
    @Autowired
    ConferenceReservationService conferenceReservationService;

    @Test
    public void should_register_a_new_account_then_use_then_close() {
        AccountId accountId = conferenceReservationService.openAccount("toto");

        // When
        conferenceReservationService.deposit(accountId, 100);
        conferenceReservationService.deposit(accountId, 200);
        conferenceReservationService.withdraw(accountId, 300);
        conferenceReservationService.closeAccount(accountId);

        // Then
        Order reloadedOrder = repository.load(accountId);
        assertThat(reloadedOrder.getStatus()).isEqualTo(CLOSED);
        assertThat(reloadedOrder.getBalance()).isEqualTo(0);
    }

    @Test
    public void should_successfully_transfer() {
        AccountId aliceAccountId = conferenceReservationService.openAccount("alice");
        conferenceReservationService.deposit(aliceAccountId, 200);
        AccountId bobAccountId = conferenceReservationService.openAccount("bob");

        // When
        conferenceReservationService.transfer(aliceAccountId, bobAccountId, 50);

        // Then
        Order aliceOrder = repository.load(aliceAccountId);
        Order bobOrder = repository.load(bobAccountId);
        assertThat(aliceOrder.getBalance()).isEqualTo(150);
        assertThat(aliceOrder.getChanges())
                .extracting(event -> tuple(event.getClass()))
                .containsExactly(
                        tuple(AccountOpened.class),
                        tuple(AccountDeposited.class),
                        tuple(TransferRequested.class),
                        tuple(TransferSent.class)
                );
        assertThat(bobOrder.getBalance()).isEqualTo(50);
        assertThat(bobOrder.getChanges())
                .extracting(event -> tuple(event.getClass()))
                .containsExactly(
                        tuple(AccountOpened.class),
                        tuple(TransferReceived.class)
                );
    }

    @Test
    public void should_fail_transfer_to_closed_account() {
        AccountId aliceAccountId = conferenceReservationService.openAccount("alice");
        conferenceReservationService.deposit(aliceAccountId, 200);
        AccountId bobAccountId = conferenceReservationService.openAccount("bob");
        conferenceReservationService.closeAccount(bobAccountId);

        // When
        conferenceReservationService.transfer(aliceAccountId, bobAccountId, 50);

        // Then
        Order aliceOrder = repository.load(aliceAccountId);
        Order bobOrder = repository.load(bobAccountId);
        assertThat(aliceOrder.getBalance()).isEqualTo(200);
        assertThat(aliceOrder.getChanges())
                .extracting(event -> tuple(event.getClass()))
                .containsExactly(
                        tuple(AccountOpened.class),
                        tuple(AccountDeposited.class),
                        tuple(TransferRequested.class),
                        tuple(TransferRefused.class)
                );
        assertThat(bobOrder.getBalance()).isEqualTo(0);
        assertThat(bobOrder.getChanges())
                .extracting(event -> tuple(event.getClass()))
                .containsExactly(
                        tuple(AccountOpened.class),
                        tuple(AccountClosed.class)
                );
    }

    @Test
    public void should_fail_transfer_when_funds_are_insufficient() {
        AccountId aliceAccountId = conferenceReservationService.openAccount("alice");
        conferenceReservationService.deposit(aliceAccountId, 200);
        AccountId bobAccountId = conferenceReservationService.openAccount("bob");

        // When
        conferenceReservationService.transfer(aliceAccountId, bobAccountId, 250);

        // Then
        Order aliceOrder = repository.load(aliceAccountId);
        Order bobOrder = repository.load(bobAccountId);
        assertThat(aliceOrder.getBalance()).isEqualTo(200);
        assertThat(aliceOrder.getChanges())
                .extracting(event -> tuple(event.getClass()))
                .containsExactly(
                        tuple(AccountOpened.class),
                        tuple(AccountDeposited.class),
                        tuple(TransferRequested.class),
                        tuple(TransferRefused.class)
                );
        assertThat(bobOrder.getBalance()).isEqualTo(0);
        assertThat(bobOrder.getChanges())
                .extracting(event -> tuple(event.getClass()))
                .containsExactly(
                        tuple(AccountOpened.class)
                );
    }
}
