package DropThatFile.engines.windowsManager;

import DropThatFile.engines.annotations.processors.Translator;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import DropThatFile.engines.LogManagement;
import org.apache.log4j.Logger;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

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
        jfxStage.setResizable(false);
    }

    /**
     * Get the Stage of the instantiate form
     * @return The Stage
     */
    public Stage getJfxStage() {
        return jfxStage;
    }

    /**
     * Show the form
     */
    public void showForm(){
        jfxStage.show();
    }

    /**
     * Hide the form
     */
    public void hideForm() { jfxStage.hide(); }

    /**
     * Close the form
     */
    public void closeForm(){ jfxStage.close(); }

    /**
     * Method for form switching
     * @param formName Simple name of the requested form
     * @param show Show the form or not yet
     * @throws Exception
     */
    public boolean goToForm(String formName, boolean show) throws Exception {
        try {
            // Instantiate the chosen form according to the input and its package
            Class<?> clazz = Class.forName("DropThatFile.engines.windowsManager.".concat(formName.equals("LoginForm") ? formName : "forms.".concat(formName)));
            Constructor<?> constructor = clazz.getConstructor(jfxStage.getClass());
            constructor.newInstance(jfxStage);
        } catch (Exception ex) {
            log.error("Unable to load \"" + formName + "\" Form.");
            ex.printStackTrace();
            return false;
        }
        // Show the form or not yet
        if (show) showForm();
        else hideForm();
        return true;
    }

    /**
     * OnClick on the "Login" button.
     */
    @Deprecated
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
    @Deprecated
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

    public void languageListening(Initializable instance, ArrayList<ImageView> langFlags){
        for (ImageView flag : langFlags) {
            flag.setOnMouseClicked(e -> {
                if(flag.getId().equals("FR")){
                    Translator.inject(instance);
                    flag.setDisable(true);
                }
            });
        }
    }
}
