package fr.soat.banking.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString(callSuper = true)
@Getter
public class AccountRegistered extends AccountEvent {

    private final String owner;
    private final String number;

    public AccountRegistered(AccountId id, String owner, String number) {
        super(id);
        this.owner = owner;
        this.number = number;
    }

    @Override
    public void applyOn(Account account) {
        account.apply(this);
    }

}
