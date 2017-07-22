package DropThatFile.engines.annotations.processors;

import DropThatFile.controllers.LoginController;
import DropThatFile.engines.annotations.Translate;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.lang.reflect.Field;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

@SupportedAnnotationTypes(value = { "DropThatFile.engines.annotations.Translate" }) //Specify which annotation Recipient process
@SupportedSourceVersion(SourceVersion.RELEASE_8) //Specify which jdk source version Recipient handle
public class Translator {
    public static void inject(Object instance) {
        // Get every field from the Object
        Field[] fields = instance.getClass().getDeclaredFields();
        // For each field annotated by the Translate annotation
        for (Field field : fields) {
            if (field.isAnnotationPresent(Translate.class) && field.isAnnotationPresent(FXML.class)) {
                Translate translate = field.getAnnotation(Translate.class);
                // For restricted fields (private, etc)
                field.setAccessible(true);
                try {
                    String fieldType = field.getType().getTypeName();
                    if(fieldType.equals(Label.class.toString())) {
                        field.set(instance, new Label(translate.translation()));
                    } else if(fieldType.equals(Button.class.toString())){
                        field.set(instance, new Button(translate.translation()));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}