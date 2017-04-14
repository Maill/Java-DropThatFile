package DropThatFile;

import DropThatFile.controllers.loginForm.LoginController;
import DropThatFile.controllers.profileForm.ProfileController;
import DropThatFile.models.Authenticator;
import DropThatFile.models.User2;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application{
    private Stage stage;
    private User2 loggedUser;
    private final double MINIMUM_WINDOW_WIDTH = 400.0;
    private final double MINIMUM_WINDOW_HEIGHT = 400.0;

    public static void main(String[] args) {
        Application.launch(Main.class, (String[])null);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        GlobalVariables.windowsManager.Init(primaryStage);
        /*try {
            stage = primaryStage;
            stage.setTitle("DropThatFile");
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            goToLogin();
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

   /* public User2 getLoggedUser() { return loggedUser; }

    public boolean userLogging(String userId, String password) throws Exception {
        if (Authenticator.validate(userId, password)) {
            loggedUser = User2.of(userId);
            goToProfile();
            return true;
        } else {
            return false;
        }
    }

    public void userLogout() throws Exception {
        loggedUser = null;
        goToLogin();
    }

    private void goToProfile() {
        try {
            ProfileController profile = (ProfileController) replaceSceneContent("ihm/profile.fxml");
            profile.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Unable to load Profile Form", ex);
        }
    }

    private void goToLogin() throws Exception{
        try {
            LoginController login = (LoginController) replaceSceneContent("ihm/login.fxml");
            login.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Unable to load Login Form", ex);
        }
    }*/

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        AnchorPane page = loader.load(in);
        in.close();
        Scene scene = new Scene(page, 600, 600);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }
}
