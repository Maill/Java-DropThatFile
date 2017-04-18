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
public @interface _Stability {
    enum Stability {UNDEFINED, UNSTABLE, STABLE}
    Stability stability() default Stability.UNDEFINED;
}