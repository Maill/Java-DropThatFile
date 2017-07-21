package DropThatFile.engines.windowsManager;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import DropThatFile.engines.LogManagement;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.io.File;
import java.lang.reflect.Constructor;

/**
 * Created by Nicolas on 02/03/2017.
 */

public class WindowsHandler {
    private final Logger log = LogManagement.getInstanceLogger(this);

    protected Stage jfxStage;

    public WindowsHandler(Stage jfxStage){
        this.jfxStage = jfxStage;
        // Application icon
        jfxStage.getIcons().add(new Image(this.getClass().getResourceAsStream( "/images/logo.png" )));
    }

    public Stage getJfxStage() {
        return jfxStage;
    }

    public void showForm(){
        jfxStage.show();
    }

    public void hideForm() { jfxStage.hide(); }

    public void closeForm(){ jfxStage.close(); }

    public void goToForm(String formName, Boolean show) throws Exception {
        try {
            // Instantiate the chosen form according to the input and its package
            Class<?> clazz = Class.forName("DropThatFile.engines.windowsManager.".concat(formName.equals("LoginForm") ? formName : "forms.".concat(formName)));
            Constructor<?> constructor = clazz.getConstructor(jfxStage.getClass());
            constructor.newInstance(jfxStage);
        } catch (Exception ex) {
            log.error("Unable to load \"" + formName + "\" Form.");
        }
        // Show the form or not yet
        if (show)
            showForm();
        else
            hideForm();
    }

    /**
     * OnClick on the "Login" button.
     */
    public boolean loginVerification(TextField email, PasswordField password) throws Exception {
        if (!email.getText().matches("^(?:(?:[a-z0-9]+\\.[a-z0-9]+)|(?:[a-z0-9]+))@(?:[a-z0-9]+(?:\\.[a-z0-9]+)+)$") || email.getText() == null){
            setMessage("Error email address", "Entered email address is incorrect.");
            return false;
        } else if(password.getText() == null || password.getText().contains(" ") || password.getText().length() < 8){
            setMessage("Error password", "Entered password is not valid.");
            return false;
        }
        return true;
    }

    /**
     *  Configure a FileChooser
     * @param fileChooser a FileChooser
     * @param initialDirectory First directory shown in the FileChooser
     * @param title FileChooser' form title
     * @param extDescription Extension description
     * @param ext Example : "*.jar" or "*.*"
     */
    public void configureFileChooser(FileChooser fileChooser, String title, String initialDirectory, String extDescription, String ext){
        fileChooser.setInitialDirectory(new File(initialDirectory));
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(extDescription, ext));
    }

    /**
     * Set a customizable message
     * @param text The text to set
     */
    public static void setMessage(String headerText, String text){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setHeaderText(headerText);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
