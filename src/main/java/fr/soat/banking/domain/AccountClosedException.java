package fr.soat.banking.domain;

public class AccountClosedException extends RuntimeException {
    public AccountClosedException(String msg) {
        super(msg);
    }
}
