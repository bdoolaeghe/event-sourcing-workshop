package fr.soat.conference.domain.order;


import fr.soat.conference.domain.*;
import fr.soat.eventsourcing.api.AggregateRoot;
import fr.soat.eventsourcing.api.DecisionFunction;
import fr.soat.eventsourcing.api.EvolutionFunction;
import lombok.Getter;

import java.util.UUID;

import static fr.soat.conference.domain.order.OrderStatus.*;

@Getter
public class Order extends AggregateRoot<AccountId> {

    private String owner;
    private String number;
    private Integer balance = 0;
    private OrderStatus status = NEW;

    public Order(AccountId accountId) {
        super(accountId);
    }

    /* aggregate evolutions  */

    @EvolutionFunction
    void apply(AccountOpened accountOpened) {
        this.owner = accountOpened.getOwner();
        this.number = accountOpened.getNumber();
        this.balance = 0;
        this.status = OPEN;
        recordChange(accountOpened);
    }

    @EvolutionFunction
    void apply(AccountDeposited accountDeposited) {
        this.balance += accountDeposited.getAmount();
        recordChange(accountDeposited);
    }

    @EvolutionFunction
    void apply(AccountWithdrawn accountWithdrawn) {
        this.balance -= accountWithdrawn.getAmount();
        recordChange(accountWithdrawn);
    }

    @EvolutionFunction
    void apply(AccountClosed accountClosed) {
        this.status = CLOSED;
        recordChange(accountClosed);
    }

    /* decisions invoked by commands */

    @DecisionFunction
    public static Order create() {
        return new Order(AccountId.next());
    }

    @DecisionFunction
    public Order open(String owner) {
        if (status != NEW) {
            throw new UnsupportedOperationException("Can not register a " + status + " account");
        }
        AccountOpened event = new AccountOpened(getId(), owner, UUID.randomUUID().toString());
        apply(event);
        return this;
    }

    @DecisionFunction
    public Order deposit(Integer amount) {
        if (status != OPEN) {
            throw new UnsupportedOperationException("Can not deposit on a " + status + " account");
        }
        AccountDeposited event = new AccountDeposited(getId(), amount);
        apply(event);
        return this;
    }

    @DecisionFunction
    public Order withdraw(Integer amount) {
        if (status != OPEN) {
            throw new UnsupportedOperationException("Can not withdraw on a " + status + " account");
        }

        if (amount > balance) {
            throw new InsufficientFundsException("Withdrawal of " + amount + " can not be applied with balance of " + balance);
        }

        AccountWithdrawn event = new AccountWithdrawn(getId(), amount);
        apply(event);
        return this;
    }

    @DecisionFunction
    public Order close() {
        if (status != OPEN) {
            throw new UnsupportedOperationException("Can not close a " + status + " account");
        }

        AccountClosed event = new AccountClosed(getId());
        apply(event);
        return this;
    }

    /* Transfer management */

    @DecisionFunction
    public Order requestTransfer(AccountId receiverAccountId, int amount) {
        if (status != OPEN) {
            throw new UnsupportedOperationException("Can not transfer from a " + status + " account");
        }
        apply(new TransferRequested(getId(), receiverAccountId, amount));
        return this;
    }

    @DecisionFunction
    public void receiveTransfer(AccountId sourceAccountId, int amount) {
        apply(new TransferReceived(getId(), sourceAccountId, amount));
    }

    @DecisionFunction
    public void refuseTransfer(AccountId targetAccountId, int amount ) {
        apply(new TransferRefused(getId(), targetAccountId, amount));
    }

    @DecisionFunction
    public void sendTransfer(AccountId targetAccountId, int amount ) {
        apply(new TransferSent(getId(), targetAccountId, amount));
    }

    @EvolutionFunction
    public void apply(TransferRequested transferRequested) {
        recordChange(transferRequested);
    }

    @EvolutionFunction
    public void apply(TransferRefused event) {
        recordChange(event);
    }

    @EvolutionFunction
    public void apply(TransferSent event) {
        this.balance -= event.getAmount();
        recordChange(event);
    }

    @EvolutionFunction
    public void apply(TransferReceived event) {
        this.balance += event.getAmount();
        recordChange(event);
    }

}
