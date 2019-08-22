package fr.soat.banking.domain;


import fr.soat.eventsourcing.api.AggregateRoot;
import fr.soat.eventsourcing.api.DecisionFunction;
import fr.soat.eventsourcing.api.Event;
import fr.soat.eventsourcing.api.EvolutionFunction;
import lombok.Getter;

import java.util.UUID;

import static fr.soat.banking.domain.AccountStatus.*;

@Getter
public class Account extends AggregateRoot<AccountId> implements TransferEventListener{

    private String owner;
    private String number;
    private Integer balance = 0;
    private AccountStatus status = NEW;

    public Account(AccountId accountId) {
        super(accountId);
    }

    /* aggregate evolutions  */

    @EvolutionFunction
    void on(AccountOpened accountOpened) {
        this.owner = accountOpened.getOwner();
        this.number = accountOpened.getNumber();
        this.balance = 0;
        this.status = OPEN;
        recordChange(accountOpened);
    }

    @EvolutionFunction
    void on(AccountDeposited accountDeposited) {
        this.balance += accountDeposited.getAmount();
        recordChange(accountDeposited);
    }

    @EvolutionFunction
    void on(AccountWithdrawn accountWithdrawn) {
        this.balance -= accountWithdrawn.getAmount();
        recordChange(accountWithdrawn);
    }

    @EvolutionFunction
    void on(AccountClosed accountClosed) {
        this.status = CLOSED;
        recordChange(accountClosed);
    }

    /* decisions invoked by commands */

    @DecisionFunction
    public static Account create() {
        return new Account(AccountId.next());
    }

    @DecisionFunction
    public Account open(String owner) {
        if (status != NEW) {
            throw new UnsupportedOperationException("Can not register a " + status + " account");
        }
        AccountOpened event = new AccountOpened(getId(), owner, UUID.randomUUID().toString());
        on(event);
        return this;
    }

    @DecisionFunction
    public Account deposit(Integer amount) {
        if (status != OPEN) {
            throw new UnsupportedOperationException("Can not deposit on a " + status + " account");
        }
        AccountDeposited event = new AccountDeposited(getId(), amount);
        on(event);
        return this;
    }

    @DecisionFunction
    public Account withdraw(Integer amount) {
        if (status != OPEN) {
            throw new UnsupportedOperationException("Can not withdraw on a " + status + " account");
        }

        if (amount > balance) {
            throw new InsufficientFundsException("Withdrawal of " + amount + " can not be applied with balance of " + balance);
        }

        AccountWithdrawn event = new AccountWithdrawn(getId(), amount);
        on(event);
        return this;
    }

    @DecisionFunction
    public Account close() {
        if (status != OPEN) {
            throw new UnsupportedOperationException("Can not close a " + status + " account");
        }

        AccountClosed event = new AccountClosed(getId());
        on(event);
        return this;
    }

    /* Transfer management */

    @DecisionFunction
    public Account requestTransfer(AccountId receiverAccountId, int amount) {
        if (status != OPEN) {
            throw new UnsupportedOperationException("Can not transfer from a " + status + " account");
        }
        on(new TransferRequested(getId(), receiverAccountId, amount));
        return this;
    }

    @DecisionFunction
    public void receiveTransfer(AccountId sourceAccountId, int amount) {
        on(new TransferReceived(getId(), sourceAccountId, amount));
    }

    @DecisionFunction
    public void refuseTransfer(AccountId targetAccountId, int amount ) {
        on(new TransferRefused(getId(), targetAccountId, amount));
    }

    @DecisionFunction
    public void sendTransfer(AccountId targetAccountId, int amount ) {
        on(new TransferSent(getId(), targetAccountId, amount));
    }

    @EvolutionFunction
    public  void on(TransferRequested transferRequested) {
        recordChange(transferRequested);
    }

    @EvolutionFunction
    public void on(TransferRefused event) {
        recordChange(event);
    }

    @EvolutionFunction
    public void on(TransferSent event) {
        this.balance -= event.getAmount();
        recordChange(event);
    }

    @EvolutionFunction
    public void on(TransferReceived event) {
        this.balance += event.getAmount();
        recordChange(event);
    }

    @Override
    @EvolutionFunction
    public void on(Event event) {
        event.applyOn(this);
    }

}
