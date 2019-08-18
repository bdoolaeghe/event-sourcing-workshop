package fr.soat.banking.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;


@NoArgsConstructor(access = PRIVATE)
@EqualsAndHashCode
@ToString(callSuper = true)
public class AccountClosed extends AccountEvent {

    public AccountClosed(AccountId accountId) {
        super(accountId);
    }

    @Override
    public void applyOn(Account account) {
        account.apply(this);
    }

}
