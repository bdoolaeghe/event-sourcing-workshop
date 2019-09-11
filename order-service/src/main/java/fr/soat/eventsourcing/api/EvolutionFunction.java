package fr.soat.eventsourcing.api;

import java.lang.annotation.*;

/**
 * Decorate an evolution function, changing the state of an aggregate
 */
@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface EvolutionFunction {
}
