package fr.soat.banking.domain.account.model;

import fr.soat.eventsourcing.api.EvolutionFunction;
import lombok.*;

import static fr.soat.util.Util.append;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class AccountOpened implements AccountEvent {

    private String owner;
    private int initialBalance;


    @Override
    @EvolutionFunction
    public Account applyOn(Account account) {
        return account.toBuilder()
                .owner(owner)
                .balance(initialBalance)
                .status(Account.Status.OPEN)
                .events(append(account.getEvents(), this))
                .build();
    }
}
