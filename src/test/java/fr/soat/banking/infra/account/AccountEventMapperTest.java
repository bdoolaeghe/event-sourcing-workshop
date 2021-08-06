package fr.soat.banking.infra.account;

import fr.soat.banking.domain.account.model.*;
import fr.soat.eventsourcing.impl.db.EventMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AccountEventMapperTest {

    @ParameterizedTest
    @MethodSource("someAccountEvents")
    void should_serdes_AccountEvent(AccountEvent event) {
        // When
        String json = EventMapper.toJson(event);
        AccountEvent deserEvent = EventMapper.fromJson(json, event.getClass());

        // Then
        assertThat(deserEvent).isEqualToComparingFieldByField(event);
    }

    private static Stream<AccountEvent> someAccountEvents() {
        return Stream.of(
                new AccountOpened("toto", 10),
                new AccountDeposited(10),
                new AccountWithdrawn(10),
                new AccountClosed()
        );
    }
}
