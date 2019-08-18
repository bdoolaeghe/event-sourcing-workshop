package fr.soat.banking.domain;

import fr.soat.eventsourcing.impl.FSEventStore;
import org.junit.Test;

import static fr.soat.banking.domain.AccountStatus.CLOSED;
import static org.assertj.core.api.Assertions.assertThat;

public class BankingServiceTest {

    FSEventStore eventStore = new FSEventStore("./target/test-classes/store1/");
    AccountRepository repository = new AccountRepository(eventStore);
    BankingService bankingService = new BankingService(repository);

    @Test
    public void should_register_a_new_account_then_use_then_close() {
        AccountId accountId = bankingService.openAccount("toto", Currency.USD);

        // When
        bankingService.deposit(accountId, Amount.of(100));
        bankingService.deposit(accountId, Amount.of(200));
        bankingService.withdraw(accountId, Amount.of(300));
        bankingService.closeAccount(accountId);

        // Then
        Account reloadedAccount = repository.load(accountId);
        assertThat(reloadedAccount.getStatus()).isEqualTo(CLOSED);
        assertThat(reloadedAccount.getBalance()).isEqualTo(Amount.of(0));
        assertThat(reloadedAccount.getCurrency()).isEqualTo(Currency.USD);
    }

    @Test
    public void should_reload_an_existing_legacy_account() {
        // A legacy account opened before we introduced the currency on account
        Account account = bankingService.loadAccount(AccountId.from("1001"));
        assertThat(account.getBalance()).isEqualTo(Amount.of(250));
        assertThat(account.getCurrency()).isEqualTo(Currency.EUR); // default value for legacy accounts is EUR
    }
}
