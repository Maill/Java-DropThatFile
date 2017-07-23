package DropThatFile.engines.annotations;


import java.lang.annotation.*;

/**
 * Created by Olivier on 25/04/2017.
 */
@Documented
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Translates {
    Translate[] value();
}


