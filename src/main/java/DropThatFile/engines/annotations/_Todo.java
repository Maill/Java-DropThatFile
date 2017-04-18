package DropThatFile.engines.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;

/**
 * Created by Olivier on 31/03/2017.
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface _Todo {
    Level level() default Level.BUG;
    //String author() default "Unknown";
    //String Recipient() default "Unknown";
    String comment() default "None.";
}