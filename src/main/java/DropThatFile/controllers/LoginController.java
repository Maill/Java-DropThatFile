package DropThatFile.controllers;

import DropThatFile.engines.LogManagement;
import DropThatFile.engines.APIData.APIModels.APIUser;
import DropThatFile.engines.windowsManager.forms.LoginForm;
import DropThatFile.engines.windowsManager.WindowsHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    private LoginForm application;

    private Stage stage = new Stage();

    private WindowsHandler windowsHandler = new WindowsHandler(stage);

    public void setApp(LoginForm application){
        this.application = application;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        email.setPromptText("Email...");
        password.setPromptText("Password...");
    }

    private boolean userLogging() throws Exception {
        String email = this.email.getText();
        String password = this.password.getText();
        try{
            if(!APIUser.Instance().login("nicolas.demoncourt@gmail.com", "aaaa")){
                setMessage("Credentials error", "Unknown credentials.");
                log.info("Des identifiants inconnus ont été saisis.\n");
                return false;
            } else {
                return true;
            }
        }
        catch(NullPointerException ex){
            log.error("Erreur anormale lors de la concordance entre les identifiants saisis et la récupération depuis la BDD.'.\nMessage : \n" + ex);
        }
        return false;
    }

    /**
     * OnClick on the "Login" button.
     */
    public void loginVerification(ActionEvent actionEvent) throws Exception {
            if(userLogging()){
                Parent root = null;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ihm/home.fxml"));
            try {
                root = loader.load();
            } catch (IOException ex) {
                log.error("Erreur au chargement du FXML de 'Home'.\nMessage : \n" + ex);
            }
            stage.setScene(new Scene(root));
            stage.show();
            ((Stage)button_login.getScene().getWindow()).close();

            try {
                windowsHandler.goToForm("HomeForm", true);
            } catch (Exception ex) {
                log.error("Erreur de 'goToForm' avec 'HomeForm' dans 'LoginController'.\nMessage : \n" + ex);
            }
        } else {
            setMessage("Unknown error", "Unhandled error");
            log.error("Erreur inconnue dans 'LoginController'.\n");
        }
    }
}
