package DropThatFile.controllers;

import DropThatFile.engines.windowsManager.LoginForm;
import DropThatFile.engines.windowsManager.userForms.MyParametersForm;
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
            setMessage("L'adresse électronique est invalide.");
        } else if(password.getText() == null || password.getText().contains(" ") || password.getText().length() < 8){
            setMessage("Mot de passe invalide.");
        } /*else if (!application.userLogging(userEmail.getText(), password.getText())) {
            setMessage("L'adresse électionique et/ou le mot de passe sont invalides.");
        }*/ else{
            Stage profileStage = new Stage();
            MyParametersForm profile = new MyParametersForm(profileStage);
            profile.show();
        }
    }

    /**
     * Set a customizable message
     * @param text The text to set
     */
    private void setMessage(String text){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
