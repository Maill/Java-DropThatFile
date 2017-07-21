package DropThatFile.controllers;

import DropThatFile.engines.LogManagement;
import DropThatFile.engines.APIData.APIModels.APIUser;
import DropThatFile.engines.windowsManager.WindowsHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static DropThatFile.engines.windowsManager.WindowsHandler.setMessage;

public class LoginController extends AnchorPane implements Initializable {
    @FXML
    TextField email;
    @FXML
    PasswordField password;
    @FXML
    Button button_login;
    @FXML
    Image image_logo;

    private Logger log = LogManagement.getInstanceLogger(this);

    private Stage stage = new Stage();

    private WindowsHandler windowsHandler = new WindowsHandler(stage);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        email.setPromptText("Email...");
        password.setPromptText("Password...");
    }

    private boolean userLogging() throws Exception {
        String email = this.email.getText();
        String password = this.password.getText();
        try{
            // Use email and password in the release
            if(!APIUser.Instance().login(email, password)){
                return false;
            } else {
                return true;
            }
        }
        catch(NullPointerException ex){
            log.error("Error with retrieved credentials from the database.'.\nMessage : \n" + ex);
        }
        return false;
    }

    /**
     * OnClick on the "Login" button.
     */
    public void loginVerification() throws Exception {
        if(userLogging()){
            try {
                windowsHandler.goToForm("HomeForm", true);
            } catch (IOException ex) {
                log.error("Error while loading the 'Home' form.\nMessage : \n" + ex);
            }
            ((Stage)button_login.getScene().getWindow()).close();
        } else {
            setMessage("Credentials error", "Unknown credentials.");
            log.error("Credentials error in the 'LoginController' form.\n");
        }
    }
}
