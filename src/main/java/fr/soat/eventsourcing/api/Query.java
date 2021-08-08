package fr.soat.eventsourcing.api;

import java.lang.annotation.*;

/**
 * Decorate a command, representing the intention of external system/user
 */
@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {
}
