package DropThatFile.engines.windowsManager;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import DropThatFile.engines.LogManagement;
import javafx.util.Pair;
import org.apache.log4j.Logger;

import java.beans.EventHandler;
import java.lang.reflect.Constructor;
import java.util.Optional;

/**
 * Created by Nicolas on 02/03/2017.
 */

public class WindowsHandler {
    private final Logger log = LogManagement.getInstanceLogger(this);

    public static Pair<String, String> auth;

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

    public void hideForm() { jfxStage.hide(); }

    public void closeForm(){ jfxStage.close(); }

    public void goToForm(String formName, Boolean showForm) throws Exception {
        try {
            // Instantiate the choosen form according to the input and its package
            Class<?> clazz = Class.forName("DropThatFile.engines.windowsManager.".concat(formName.equals("LoginForm") ? formName : "forms.".concat(formName)));
            //Class<?> clazz = Class.forName("DropThatFile.engines.windowsManager.forms.".concat(formName));
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

    public void setAuthentication(){
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Authentication Dialog");
        dialog.setHeaderText("Please enter your login and password");

        // Set the icon.
        //dialog.setGraphic(new ImageView(this.getClass().getClassLoader().getResourceAsStream("C:\\Users\\Olivier\\IdeaProjects\\Java-DropThatFile\\src\\resources\\images\\logo.png").toString()));

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField email = new TextField();
        email.setPromptText("Email");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        grid.add(new Label("Email:"), 0, 0);
        grid.add(email, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation.
        email.textProperty().addListener((observable, oldValue, newValue) -> loginButton.setDisable(newValue.trim().isEmpty()));

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> email.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        final boolean[] validation = {false};
        dialog.setResultConverter((ButtonType dialogButton) -> {
            if (dialogButton.equals(loginButtonType)) {
                try {
                    if (true/*loginVerification(email, password)*/){ // Authentication validations
                        validation[0] = true;
                        return new Pair<>(email.getText(), password.getText());
                    } else {
                        validation[0] = false;
                    }
                } catch(Exception ex){
                    log.trace("Error while logging!", ex);
                }
            }
            return null;
        });

        // Retrieve authentication informations in a Pair
        Optional<Pair<String, String>> result = dialog.showAndWait();
        //result.ifPresent(emailPassword -> System.out.println("Email=" + emailPassword.getKey() + ", Password=" + emailPassword.getValue()));

        // If validations are OK: we go to the HomeForm
        if (validation[0] == true){
            try{
                goToForm("HomeForm", true);
            } catch (Exception ex){
                log.trace("Error while opening HomeForm via authentication modal!", ex);
            }
        } else if(validation[0] == false){ // Else, we recursively retry
            setAuthentication();
        }
    }

    /**
     * OnClick on the "Login" button.
     */
    /*public boolean loginVerification(TextField email, PasswordField password) throws Exception {
        if (!email.getText().matches("^(?:(?:[a-z0-9]+\\.[a-z0-9]+)|(?:[a-z0-9]+))@(?:[a-z0-9]+(?:\\.[a-z0-9]+)+)$") || email.getText() == null){
            setMessage("Error email address", "Entered email address is incorrect.");
            return false;
        } else if(password.getText() == null || password.getText().contains(" ") || password.getText().length() < 8){
            setMessage("Error password", "Entered password is not valid.");
            return false;
        }else if (!application.userLogging(userEmail.getText(), password.getText())) {
            setMessage("L'adresse électronique et/ou le mot de passe sont inconnus.");
            return false;
        }
        return true;
    }*/

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
