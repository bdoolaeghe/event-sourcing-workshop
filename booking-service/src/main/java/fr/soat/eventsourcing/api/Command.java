package fr.soat.eventsourcing.api;

import java.lang.annotation.*;

/**
 * Decorate a command, representing the intention of external system/user
 */
@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
}
