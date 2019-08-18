package fr.soat.banking.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@EqualsAndHashCode
@ToString(callSuper = true)
@Getter
public class AccountOpened extends AccountEvent {

    private String owner;
    private String number;

    public AccountOpened(AccountId id, String owner, String number) {
        super(id);
        this.owner = owner;
        this.number = number;
    }

    @Override
    public void applyOn(Account account) {
        account.apply(this);
    }

}
