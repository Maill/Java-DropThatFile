package DropThatFile.engines.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationParsing {

    public static void main(String[] args) {
        try {
            for (Method method : AnnotationParsing.class.getClassLoader()
                    .loadClass(("com.journaldev.annotations.AnnotationExample")).getMethods()) {
                // checks if MethodInfo annotation is present for the method
                if (method.isAnnotationPresent(DropThatFile.engines.annotations._Stability.class)) {
                    try {
                        // iterates all the annotations available in the method
                        for (Annotation anno : method.getDeclaredAnnotations()) {
                            System.out.println("Annotation in Method '" + method + "' : " + anno);
                        }
                        _Stability stabilityAnno;
                        stabilityAnno = method.getAnnotation(_Stability.class);
                        if (stabilityAnno.stability().equals("UNDEFINED")) {
                            System.out.println("UNDEFINED methods = " + method);
                        }
                        if (stabilityAnno.stability().equals("UNSTABLE")){
                            System.out.println("UNSTABLE methods = " + method);
                        }

                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}