package DropThatFile.engines.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Olivier on 31/03/2017.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Todo {
    enum Priority {LOW, MEDIUM, HIGH}
    enum Status {STARTED, NOT_STARTED}
    Priority priority() default Priority.LOW;
    Status status() default Status.NOT_STARTED;
}