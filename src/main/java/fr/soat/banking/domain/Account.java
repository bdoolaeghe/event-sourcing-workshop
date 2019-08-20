package fr.soat.banking.domain;


import fr.soat.eventsourcing.api.AggregateRoot;
import fr.soat.eventsourcing.api.DecisionFunction;
import fr.soat.eventsourcing.api.EvolutionFunction;
import lombok.Getter;

import java.util.UUID;

import static fr.soat.banking.domain.AccountStatus.*;

@Getter
public class Account extends AggregateRoot<AccountId> {

    private String owner;
    private String number;
    private Integer balance = 0;
    private AccountStatus status = NEW;

    public Account(AccountId accountId) {
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
    public static Account create() {
        return new Account(AccountId.next());
    }

    @DecisionFunction
    public Account open(String owner) {
        if (status != NEW) {
            throw new UnsupportedOperationException("Can not register a " + status + " account");
        }
        AccountOpened event = new AccountOpened(getId(), owner, UUID.randomUUID().toString());
        apply(event);
        return this;
    }

    @DecisionFunction
    public Account deposit(Integer amount) {
        if (status != OPEN) {
            throw new UnsupportedOperationException("Can not deposit on a " + status + " account");
        }
        AccountDeposited event = new AccountDeposited(getId(), amount);
        apply(event);
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

    /* Transfer management */

    @DecisionFunction
    public Account requestTransfer(Account targetAccount, int amount) {
        if (status != OPEN) {
            throw new UnsupportedOperationException("Can not transfer from a " + status + " account");
        }

        apply(new TransferRequested(getId(), targetAccount.getId(), amount));

        if (amount <= balance) {
            targetAccount.receiveTransfer(this, amount);
        } else {
            apply(new TransferRefused(getId(), targetAccount.getId(), amount));
        }

        return this;
    }

    @DecisionFunction
    public void receiveTransfer(Account sourceAccount, int amount) {
        if (status != OPEN) {
            sourceAccount.apply(new TransferRefused(sourceAccount.getId(), getId(), amount));
        } else {
            apply(new TransferReceived(sourceAccount.getId(), getId(), amount));
            sourceAccount.apply(new TransferSent(sourceAccount.getId(), getId(), amount));
        }
    }

    @EvolutionFunction
    void apply(TransferRequested transferRequested) {
        recordChange(transferRequested);
    }

    @EvolutionFunction
    void apply(TransferRefused event) {
        recordChange(event);
    }

    @EvolutionFunction
    void apply(TransferSent event) {
        this.balance -= event.getAmount();
        recordChange(event);
    }

    @EvolutionFunction
    void apply(TransferReceived event) {
        this.balance += event.getAmount();
        recordChange(event);
    }
}
