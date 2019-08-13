package fr.soat.banking.domain;


import fr.soat.eventsourcing.api.Aggregate;
import fr.soat.eventsourcing.api.DecisionFunction;
import fr.soat.eventsourcing.api.Event;
import fr.soat.eventsourcing.api.EvolutionFunction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Account implements Aggregate<AccountId> {

    private final AccountId accountId;
    private String owner;
    private String number;
    private boolean closed = false;
    private Integer balance = 0;
    private List<Event> changes = new ArrayList<>();
    private int version = 0;

    public static Account hydrate(AccountId accountId, List<AccountEvent> events) {
        Account account = new Account(accountId);
        events.forEach(event -> event.applyOn(account));
        return account;
    }

    @Override
    public AccountId getId() {
        return accountId;
    }

    /* aggregate evolutions  */

    @EvolutionFunction
    void apply(AccountRegistered accountRegistered) {
        this.owner = accountRegistered.getOwner();
        this.number = accountRegistered.getNumber();
        this.balance = 0;
        this.closed = false;
        this.version++;
    }

    @EvolutionFunction
    void apply(AccountDeposited accountDeposited) {
        this.balance += accountDeposited.getAmount();
        this.version++;
    }

    @EvolutionFunction
    void apply(AccountWithdrawn accountWithdrawn) {
        this.balance -= accountWithdrawn.getAmount();
        this.version++;
    }

    @EvolutionFunction
    void apply(AccountClosed accountClosed) {
        this.closed = true;
        this.version++;
    }

    /* commands on aggregate */

    @DecisionFunction
    public static Account create() {
        return new Account(AccountId.next());
    }

    @DecisionFunction
    public Account register(String owner) {
        AccountRegistered event = new AccountRegistered(getId(), owner, UUID.randomUUID().toString());
        changes.add(event);
        event.applyOn(this);
        return this;
    }

    @DecisionFunction
    public Account deposit(Integer amount) {
        AccountDeposited event = new AccountDeposited(getId(), amount);
        changes.add(event);
        event.applyOn(this);
        return this;
    }

    @DecisionFunction
    public Account withdraw(Integer amount) {
        if (amount > balance) {
            throw new InsufficientFundsException("Withdrawal of " + amount + " can not be applied with balance of " + balance);
        }

        AccountWithdrawn event = new AccountWithdrawn(getId(), amount);
        changes.add(event);
        event.applyOn(this);
        return this;
    }

    @DecisionFunction
    public Account close() {
        if (isClosed()) {
            throw new AccountClosedException("Failed to close account " + getId() + " (already closed)");
        }

        AccountClosed event = new AccountClosed(getId());
        changes.add(event);
        event.applyOn(this);
        return this;
    }

}
