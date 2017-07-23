package DropThatFile.engines.annotations.processors;

import DropThatFile.engines.annotations.Translate;
import javafx.fxml.FXML;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

@SupportedAnnotationTypes(value = { "DropThatFile.engines.annotations.Translate" }) //Specify which annotation Recipient process
@SupportedSourceVersion(SourceVersion.RELEASE_8) //Specify which jdk source version Recipient handle
public class Translator {
    public static Object inject(Object instance) {
        // Get every field from the Object
        Field[] fields = instance.getClass().getDeclaredFields();
        // For restricted fields (private, etc)
        AccessibleObject.setAccessible(fields, true);
        // For each field annotated by the Translate annotation
        for (Field field : fields) {
            if (field.isAnnotationPresent(Translate.class) && field.isAnnotationPresent(FXML.class)) {
                Translate translate = field.getAnnotation(Translate.class);
                try {
                    setAll(instance, field, translate);
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    private static void setText(Object instance, Field field, String methodName, String fieldName, Translate translate)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method;
        if(field.getName().equals(fieldName)) {
            method = instance.getClass().getMethod(methodName, String.class);
            method.setAccessible(true);
            method.invoke(instance, translate.translation());
        }
    }

    private static void setAll(Object instance, Field field, Translate translate)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        setText(instance, field, "setLabelEmail", "label_email", translate);
        setText(instance, field, "setLabelPassword", "label_password", translate);
        setText(instance, field, "setButtonLogin", "button_login", translate);
        setText(instance, field, "setLabelFolderName", "label_folderName", translate);
        setText(instance, field, "setButtonCreateFolder", "createFolder_button", translate);
        setText(instance, field, "setButtonRemoveFolder", "removeFolder_button", translate);
        setText(instance, field, "setTabMyRepository", "myRepository_tab", translate);
        setText(instance, field, "setTabHome", "home_tab", translate);
        setText(instance, field, "setCheckBoxAutoRefresh", "checkBox_autoRefresh", translate);
    }
}