package fr.soat.banking.domain;


import com.google.common.base.Preconditions;
import fr.soat.eventsourcing.api.AggregateRoot;
import fr.soat.eventsourcing.api.DecisionFunction;
import fr.soat.eventsourcing.api.EvolutionFunction;
import lombok.Getter;

import java.util.Optional;
import java.util.UUID;

import static fr.soat.banking.domain.AccountStatus.*;

@Getter
public class Account extends AggregateRoot<AccountId> {

    private String owner;
    private String number;
    private Currency currency;
    private Amount balance;
    private AccountStatus status = NEW;

    public Account(AccountId accountId) {
        super(accountId);
    }

    /* aggregate evolutions  */

    @EvolutionFunction
    void apply(AccountOpened accountOpened) {
        this.owner = accountOpened.getOwner();
        this.number = accountOpened.getNumber();
        this.balance = Amount.of(0);
        this.status = OPEN;
        // for retro compatibility, currency is EUR when unspecified at account opening
        this.currency = Optional.ofNullable(accountOpened.getCurrency()).orElse(Currency.EUR);
        recordChange(accountOpened);
    }

    @EvolutionFunction
    void apply(AccountDeposited accountDeposited) {
        this.balance = this.balance.plus(accountDeposited.getDepositedAmount());
        recordChange(accountDeposited);
    }

    @EvolutionFunction
    void apply(AccountWithdrawn accountWithdrawn) {
        this.balance = this.balance.minus(accountWithdrawn.getWithdrawnAmount());
        recordChange(accountWithdrawn);
    }

    @EvolutionFunction
    void apply(AccountClosed accountClosed) {
        this.status = CLOSED;
        recordChange(accountClosed);
    }

    /* decisions invoked by commands */

    @DecisionFunction
    public static Account create(AccountId id) {
        return new Account(id);
    }

    @DecisionFunction
    public Account open(String owner, Currency currency) {
        Preconditions.checkNotNull(currency, "currency is mandatory");
        if (status != NEW) {
            throw new UnsupportedOperationException("Can not register a " + status + " account");
        }
        AccountOpened event = new AccountOpened(getId(), owner, currency, UUID.randomUUID().toString());
        apply(event);
        return this;
    }

    @DecisionFunction
    public Account deposit(Amount amount) {
        if (status != OPEN) {
            throw new UnsupportedOperationException("Can not deposit on a " + status + " account");
        }
        AccountDeposited event = new AccountDeposited(getId(), amount);
        apply(event);
        return this;
    }

    @DecisionFunction
    public Account withdraw(Amount amount) {
        if (status != OPEN) {
            throw new UnsupportedOperationException("Can not withdraw on a " + status + " account");
        }

        if (amount.greaterThan(balance)) {
            throw new InsufficientFundsException("Withdrawal of " + amount + " can not be applied with balance of " + balance);
        }

        AccountWithdrawn event = new AccountWithdrawn(getId(), amount);
        apply(event);
        return this;
    }

    @DecisionFunction
    public Account close() {
        if (status != OPEN) {
            throw new UnsupportedOperationException("Can not close a " + status + " account");
        }

        AccountClosed event = new AccountClosed(getId());
        apply(event);
        return this;
    }

}
