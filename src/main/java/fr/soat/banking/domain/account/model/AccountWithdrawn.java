package fr.soat.banking.domain.account.model;

import fr.soat.eventsourcing.api.EvolutionFunction;
import lombok.*;

import static fr.soat.util.Util.append;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class AccountWithdrawn implements AccountEvent {

    private Integer amount;

    @Override
    @EvolutionFunction
    public Account applyOn(Account account) {
        return account.toBuilder()
                .balance(account.getBalance() - amount)
                .events(append(account.getEvents(), this))
                .build();
    }
}
