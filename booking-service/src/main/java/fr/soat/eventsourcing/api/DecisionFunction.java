package fr.soat.eventsourcing.api;

import java.lang.annotation.*;

/**
 * decorate a decision function, containing business logic in a command context
 */
@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface DecisionFunction{
}
