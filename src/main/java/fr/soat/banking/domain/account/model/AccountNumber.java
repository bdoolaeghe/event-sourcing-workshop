package fr.soat.banking.domain.account.model;

import fr.soat.eventsourcing.api.EntityId;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public final class AccountNumber implements EntityId {

    @Getter
    private String number;

    public static AccountNumber from(String accountNumber) {
        return new AccountNumber(accountNumber);
    }

    @Override
    public String getIdValue() {
        return getNumber();
    }
}
