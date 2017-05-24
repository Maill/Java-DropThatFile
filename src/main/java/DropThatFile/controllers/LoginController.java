package DropThatFile.controllers;

import DropThatFile.GlobalVariables;
import DropThatFile.engines.LogManagement;
import DropThatFile.engines.annotations.Level;
import DropThatFile.engines.annotations._Todo;
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
    Button login;
    @FXML
    Label loginMessage;
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

    @_Todo(level = Level.EVOLUTION, comment = "Implémenter encryptage du mot de passe de l'utilisateur dans User.")
    private boolean userLogging() throws Exception {
        String email = this.email.getText();
        String password = this.password.getText();
        if(email.equals(GlobalVariables.currentUser.getEmail())){
            if(password.equals(GlobalVariables.currentUser.getPassword())){
                return true;
            } else {
                setMessage("Password error", "Unknown password.");
                log.info("Un password inconnu a été saisi.\nPassword :" + password);
                return false;
            }
        } else {
            setMessage("Email error", "Unknown email.");
            log.info("Un email inconnu a été saisi.\nEmail :" + email);
            return false;
        }
    }

    /**
     * OnClick on the "Login" button.
     */
    public void loginVerification(ActionEvent actionEvent) throws Exception {
        if (!email.getText().matches("^(?:(?:[a-z0-9]+\\.[a-z0-9]+)|(?:[a-z0-9]+))@(?:[a-z0-9]+(?:\\.[a-z0-9]+)+)$") || email.getText() == null){
            setMessage("Error email address", "Email address is incorrect.");
        } else if(password.getText() == null || password.getText().contains(" ") || password.getText().length() < 8){
            setMessage("Error password", "Password is incorrect.");
        }else if(!userLogging()){
            setMessage("Logging error", "No match for email and/or password.");
        }
        else if(userLogging()){
            Parent root = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ihm/home.fxml"));
            try {
                root = loader.load();
            } catch (IOException ex) {
                log.error("Erreur au chargement du FXML de 'Home'.\nMessage : \n" + ex);
            }
            HomeController controller = loader.getController();
            stage.setScene(new Scene(root));
            stage.show();
            ((Stage)login.getScene().getWindow()).close();

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
