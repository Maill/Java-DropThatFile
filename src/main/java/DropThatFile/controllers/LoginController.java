package DropThatFile.controllers;

import DropThatFile.engines.windowsManager.LoginForm;
import DropThatFile.engines.windowsManager.WindowsHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends AnchorPane implements Initializable {

    @FXML
    TextField userEmail;
    @FXML
    PasswordField password;
    @FXML
    Button login;
    @FXML
    Label loginMessage;

    private LoginForm application;

    private Stage stage = new Stage();

    private WindowsHandler windowsHandler = new WindowsHandler(stage);
    
    public void setApp(LoginForm application){
        this.application = application;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //userEmail.setPromptText("Email...");
        //password.setPromptText("Password...");
    }

    /**
     * OnClick on the "Login" button.
     * @param event
     */
    public void loginVerification(ActionEvent event) throws Exception {
        if (!userEmail.getText().matches("^(?:(?:[a-z0-9]+\\.[a-z0-9]+)|(?:[a-z0-9]+))@(?:[a-z0-9]+(?:\\.[a-z0-9]+)+)$") || userEmail.getText() == null){
            setMessage("Email address is incorrect.");
        } else if(password.getText() == null || password.getText().contains(" ") || password.getText().length() < 8){
            setMessage("Password is not valid.");
        } /*else if (!application.userLogging(userEmail.getText(), password.getText())) {
            setMessage("L'adresse électronique et/ou le mot de passe sont invalides.");
        }*/
        else{ // Si tout est OK, on se connecte
            windowsHandler.goToForm("MyHomeForm", true);
        }
    }

    /**
     * Set a customizable message
     * @param text The text to set
     */
    private void setMessage(String text){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
