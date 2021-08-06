package fr.soat.banking.domain.account.model;


import fr.soat.eventsourcing.api.DecisionFunction;
import fr.soat.eventsourcing.api.Entity;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Value
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Account implements Entity<AccountNumber, AccountEvent> {

    public enum Status {
        NEW,
        OPEN,
        CLOSED
    }

    AccountNumber number;
    String owner;
    Integer balance;
    Status status;

    List<AccountEvent> events;
    int version;

    @Override
    public AccountNumber getId() {
        return getNumber();
    }

    public static Account create() {
        return create(null, 0);
    }

    public static Account create(AccountNumber number, int version) {
        return new Account(
                number,
                "",
                0,
                Status.NEW,
                emptyList(),
                version
        );
    }

    /* decisions invoked by commands */

    @DecisionFunction
    public Account register(String owner, int initialDeposit) {
        if (status != Status.NEW) {
            throw new UnsupportedOperationException("Can not register a " + status + " account");
        }
        return new AccountOpened(owner, initialDeposit).applyOn(this);
    }

    @DecisionFunction
    public Account deposit(Integer amount) {
        if (status != Status.OPEN) {
            throw new UnsupportedOperationException("Can not deposit on a " + status + " account");
        }
        return new AccountDeposited(amount).applyOn(this);
    }

    @DecisionFunction
    public Account withdraw(Integer amount) {
        if (status != Status.OPEN) {
            throw new UnsupportedOperationException("Can not withdraw on a " + status + " account");
        }

        if (amount > balance) {
            throw new InsufficientFundsException("Withdrawal of " + amount + " can not be applied with balance of " + balance);
        }

        return new AccountWithdrawn(amount).applyOn(this);
    }

    @DecisionFunction
    public Account close() {
        if (status != Status.OPEN) {
            throw new UnsupportedOperationException("Can not close a " + status + " account");
        }

        return new AccountClosed().applyOn(this);
    }

}
