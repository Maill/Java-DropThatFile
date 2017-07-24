package DropThatFile.engines.annotations.processors;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

//INFO : Annotations used in here must exist and accessible.
import DropThatFile.engines.annotations._Todo;

@Deprecated
@SupportedAnnotationTypes(value = { "DropThatFile.engines.annotations._Todo" }) //Specifie which annotation Recipient process
@SupportedSourceVersion(SourceVersion.RELEASE_8) //Specifie which jdk source version Recipient handle
public class TodoProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //Parsing every annotation concerned by this processor
        for (TypeElement te : annotations) {
            System.out.println("Annotation processing " + te.getQualifiedName());

            //Retrieves all annotated elements with the current annotation
            for (Element element : roundEnv.getElementsAnnotatedWith(te)) {
                String name = element.getClass().toString();

                System.out.println("----------------------------------");

               //Knows what type of element is annotated (constructor, parameter, class, etc)
                System.out.println("\n Type of annotated element : " + element.getKind() + "\n");

                //Returns the name of the annotated element, its variable name, its class name, etc
                System.out.println("\t --> Element processing : "+ element.getSimpleName() + "\n");

                //Some informations on the annotated element
                System.out.println("enclosed elements : " + element.getEnclosedElements());
                System.out.println("as type : " + element.asType());
                System.out.println("enclosing element : " + element.getEnclosingElement() + "\n");

                //Retrieval of our annotation
                _Todo todo = element.getAnnotation(_Todo.class);

                //If it is retrieved (not null), then we retrieve its content
                if (todo != null) {
                    System.out.println("\t\t Level : " + todo.level());
                    System.out.println("\t\t Comment : " + todo.comment());
                }
            }
        }
        return true;
    }
}