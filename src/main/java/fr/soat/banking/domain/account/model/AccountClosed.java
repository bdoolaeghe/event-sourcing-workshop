package fr.soat.banking.domain.account.model;

import fr.soat.eventsourcing.api.EvolutionFunction;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static fr.soat.util.Util.append;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class AccountClosed implements AccountEvent {

    @Override
    @EvolutionFunction
    public Account applyOn(Account account) {
        return account.toBuilder()
                .status(Account.Status.CLOSED)
                .events(append(account.getEvents(), this))
                .build();
    }
}
