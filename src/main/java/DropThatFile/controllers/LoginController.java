package DropThatFile.controllers;

import DropThatFile.engines.FilesJobs;
import DropThatFile.engines.LogManagement;
import DropThatFile.engines.APIData.APIModels.APIUser;
import DropThatFile.engines.annotations.Translate;
import DropThatFile.engines.windowsManager.WindowsHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static DropThatFile.GlobalVariables.currentUser;
import static DropThatFile.GlobalVariables.currentUserRepoPath;
import static DropThatFile.GlobalVariables.userRepoMainPath;
import static DropThatFile.engines.windowsManager.WindowsHandler.setMessage;


@SupportedAnnotationTypes(value = { "DropThatFile.engines.annotations.Translate" }) //Specify which annotation Recipient process
@SupportedSourceVersion(SourceVersion.RELEASE_8) //Specify which jdk source version Recipient handle
public class LoginController extends AnchorPane implements Initializable {
    //region FXML attributes
    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    @Translate(translation = "Connexion")
    private Button button_login;

    @FXML
    @Translate(translation = "Email :")
    private Label label_email;

    @FXML
    @Translate(translation = "Mot de passe :")
    private Label label_password;

    //@FXML
    //ImageView imageView_flagEN;

    @FXML
    ImageView imageView_flagFR;
    //endregion

    private Logger log = LogManagement.getInstanceLogger(this);

    private Stage stage = new Stage();

    private WindowsHandler windowsHandler = new WindowsHandler(stage);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<ImageView> langFlags = new ArrayList<>();
        //langFlags.add(imageView_flagEN);
        langFlags.add(imageView_flagFR);
        windowsHandler.languageListening(this, langFlags);
        email.setPromptText("Email...");
        password.setPromptText("Password...");
    }

    /**
     * User authentication with the API
     * @return A boolean stating the result from the authentication
     * @throws Exception
     */
    private boolean userLogging() throws Exception {
        String email = this.email.getText();
        String password = this.password.getText();
        try {
            // User authentication
            if(!APIUser.Instance().login(email, password)){
                return false;
            } else {
                // Set the repository path of the user
                String firstCharFName = currentUser.getfName().substring(0,1);
                String lName = currentUser.getlName();
                currentUserRepoPath = userRepoMainPath.concat(firstCharFName + lName + "\\");
                return true;
            }
        } catch(NullPointerException ex){
            log.error("Error with retrieved credentials from the database.'.\nMessage : \n" + ex);
        }
        return false;
    }

    /**
     * OnClick on the "Login" button.
     * @throws Exception
     */
    public void loginVerification() throws Exception {
        if(userLogging()){
            try {
                // Get files from FTP
                FilesJobs.Instance().downloadFiles();
                if(!windowsHandler.goToForm("HomeForm", true)) return;
                ((Stage)button_login.getScene().getWindow()).close();
            } catch (IOException ex) {
                log.error("Error while loading the 'Home' form.\nMessage : \n" + ex);
            }
        } else {
            setMessage("Credentials error", "Unknown credentials.");
            log.error("Credentials error in the 'LoginController' form.\n");
        }
    }

    //region
    public void setLabelEmail(String str){
        label_email.setText(str);
    }

    public void setLabelPassword(String str){
        label_password.setText(str);
    }

    public void setButtonLogin(String str){
        button_login.setText(str);
    }
    //endregion
}
