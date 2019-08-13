package fr.soat.banking.domain;

import fr.soat.eventsourcing.api.AggregateId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString(of = "value")
public final class AccountId implements AggregateId {

    @Getter
    private final String value;

    public static AccountId next() {
        return new AccountId(String.valueOf(AggregateId.idGenerator.getAndIncrement()));
    }

    public static AccountId from(String id) {
        return new AccountId(id);
    }
}
