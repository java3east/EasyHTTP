package org.javabeast.easy.http;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Set the required Arguments with their name for an HTTPHandler<br>
 * If the Request doesn't contain an Argument with this name and type,<br>
 * the httpserver will return an error, and the handler won't be called.
 * @author Java3east
 * @version 1.0
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface HandlerRule {
    /**
     * @return the name of the required argument
     */
    @NotNull String argument();

    /**
     * @return the type (Class) of the required argument
     */
    @NotNull  Class<?> type();
}
