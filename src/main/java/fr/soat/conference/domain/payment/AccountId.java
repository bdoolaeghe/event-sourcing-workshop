package fr.soat.conference.domain.payment;

import fr.soat.eventsourcing.api.EntityId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString(of = "value")
public final class AccountId implements EntityId {

    @Getter
    private final String value;

    public static AccountId from(String id) {
        return new AccountId(id);
    }
}
