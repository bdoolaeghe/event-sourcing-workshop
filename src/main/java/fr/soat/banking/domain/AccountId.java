package fr.soat.banking.domain;

import fr.soat.eventsourcing.api.AggregateId;
import lombok.*;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
@EqualsAndHashCode
@ToString(of = "value")
public final class AccountId implements AggregateId {

    @Getter
    private String value;

    public static AccountId from(String id) {
        return new AccountId(id);
    }
}
