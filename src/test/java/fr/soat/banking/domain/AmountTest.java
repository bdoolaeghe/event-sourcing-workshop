package fr.soat.banking.domain;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;


public class AmountTest {

    @Test
    public void should_parse_amount() {
        assertThat(Amount.of(1.234f).toString()).isEqualTo("1.23");
        assertThat(Amount.of(1.237f).toString()).isEqualTo("1.24");
        assertThat(Amount.of(1.00f).toString()).isEqualTo("1.0");
        assertThat(Amount.of(1).toString()).isEqualTo("1.0");
    }

    @Test
    public void should_add() {
        assertThat(Amount.of(1.23f).plus(Amount.of(2.23f)).toString()).isEqualTo("3.46");
        assertThat(Amount.of(1.234f).plus(Amount.of(0.002f)).toString()).isEqualTo("1.23");
    }

    @Test
    public void should_remove() {
        assertThat(Amount.of(1.23f).minus(Amount.of(0.23f)).toString()).isEqualTo("1.0");
        assertThat(Amount.of(1.236f).minus(Amount.of(0.002f)).toString()).isEqualTo("1.24");
    }

    @Test
    public void should_multiply() {
        assertThat(Amount.of(1.0f).multipliedBy(2f).toString()).isEqualTo("2.0");
        assertThat(Amount.of(1.234f).multipliedBy(2f).toString()).isEqualTo("2.46");
        assertThat(Amount.of(10f).multipliedBy(1.1f).toString()).isEqualTo("11.0");
    }
}
