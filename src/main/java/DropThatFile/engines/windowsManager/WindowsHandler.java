package DropThatFile.engines.windowsManager;

import javafx.stage.Stage;
import DropThatFile.engines.LogManagement;
import java.lang.reflect.Constructor;

/**
 * Created by Nicolas on 02/03/2017.
 */

public class WindowsHandler {
    private final org.apache.log4j.Logger log = LogManagement.getInstanceLogger(this);
    protected Stage jfxStage;

    public WindowsHandler(Stage jfxStage){
        this.jfxStage = jfxStage;
    }

    public Stage getJfxStage() {
        return jfxStage;
    }

    public void showForm(){
        jfxStage.show();
    }

    public void goToForm(String formName, Boolean showForm) throws Exception {
        try {
            // Instantiate the choosen form according to the input and its package
            Class<?> clazz = Class.forName("DropThatFile.engines.windowsManager.".concat(formName.equals("LoginForm") ? formName : "forms.".concat(formName)));
            Constructor<?> constructor = clazz.getConstructor(jfxStage.getClass());
            constructor.newInstance(jfxStage);
        } catch (Exception ex) {
            log.error("Unable to load \"" + formName + "\" Form.");
        }
        // Show it or not
        if (showForm == true) {
            jfxStage.show();
        } else {
            jfxStage.hide();
        }
    }
}
