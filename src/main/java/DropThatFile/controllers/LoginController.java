package DropThatFile.controllers;

import DropThatFile.engines.windowsManager.forms.LoginForm;
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
     */
    public void loginVerification(ActionEvent actionEvent) throws Exception {
        if (!userEmail.getText().matches("^(?:(?:[a-z0-9]+\\.[a-z0-9]+)|(?:[a-z0-9]+))@(?:[a-z0-9]+(?:\\.[a-z0-9]+)+)$") || userEmail.getText() == null){
            windowsHandler.setMessage("Error email address", "Entered email address is incorrect.");
        } else if(password.getText() == null || password.getText().contains(" ") || password.getText().length() < 8){
            windowsHandler.setMessage("Error password", "Entered password is not valid.");
        }/*else if (!application.userLogging(userEmail.getText(), password.getText())) {
            windowsHandler.setMessage("L'adresse électronique et/ou le mot de passe sont inconnus.");
        }*/
        else {
            windowsHandler.goToForm("HomeForm", true);
        }
    }
}
