package fr.soat.banking.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Amount {

    private String value;

    private Amount(float amount) {
        this.value = String.valueOf(round2digits(amount));
    }

    private float round2digits(float amount) {
        return Math.round(amount * 100) / 100f;
    }

    private float asFloat() {
        return Float.parseFloat(value);
    }

    public static Amount of(int amount) {
        return Amount.of((float)amount);
    }

    public static Amount of(float amount) {
        return new Amount(amount);
    }

    @Override
    public String toString() {
        return value;
    }

    public Amount plus(Amount anotherAmount) {
        return Amount.of(asFloat() + round2digits(anotherAmount.asFloat()));
    }

    public Amount minus(Amount anotherAmount) {
        return Amount.of(asFloat() - round2digits(anotherAmount.asFloat()));
    }

    public Amount multipliedBy(float factor) {
        return Amount.of(asFloat() * factor);
    }

    public boolean greaterThan(Amount anotherAmount) {
        return asFloat() > anotherAmount.asFloat();
    }
}
