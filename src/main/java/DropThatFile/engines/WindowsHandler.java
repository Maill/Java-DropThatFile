package DropThatFile.engines;

import DropThatFile.Main;
import DropThatFile.controllers.loginForm.LoginController;
import DropThatFile.controllers.profileForm.ProfileController;
import DropThatFile.models.Authenticator;
import DropThatFile.models.User2;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Nicolas on 02/03/2017.
 */

public class WindowsHandler {
    private final double MINIMUM_WINDOW_WIDTH = 400.0;
    private final double MINIMUM_WINDOW_HEIGHT = 400.0;

    private User2 loggedUser;
    private Object currentWindow; // Type Object car on ne connait pas précisement le type de fenêtre active.

    public Object getCurrentWindow() {
        return currentWindow;
    }

    public void Init(Stage primaryStage)throws Exception{
        currentWindow = new WindowsHandler.loginForm(primaryStage);
        EntryPoint();
    }

    public void EntryPoint() throws Exception{
        // La fenêtre qui s'ouvre en premier doit être la fenêtre de connexion
        ((loginForm) currentWindow).setAndShowWindow();
    }

    public class loginForm{

        private Stage currentStage;

        public Stage getCurrentStage() {
            return currentStage;
        }

        public loginForm(Stage primaryStage){
            currentStage = primaryStage;
        }

        public void setWindow() throws Exception{
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ihm/login.fxml"));
            try {
                currentStage.setTitle("DropThatFile");
                currentStage.setScene(new Scene(root));
                currentStage.setMinWidth(MINIMUM_WINDOW_WIDTH);
                currentStage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
                goToLogin();
                currentStage.show();
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void showWindow(){
            currentStage.show();
        }

        public void setAndShowWindow() throws Exception{
            setWindow();
            showWindow();
        }


        public User2 getLoggedUser() { return loggedUser; }

        public boolean userLogging(String userId, String password) throws Exception{
            if (Authenticator.validate(userId, password)) {
                loggedUser = User2.of(userId);
                goToProfile();
                return true;
            } else {
                return false;
            }
        }

        public void userLogout(){
            loggedUser = null;
            goToLogin();
        }

        public void goToProfile() {
            try {
                ProfileController profile = (ProfileController) replaceSceneContent("profile.fxml");
                profile.setApp(this);
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void goToLogin() {
            try {
                LoginController login = (LoginController) replaceSceneContent("login.fxml");
                login.setApp(this);
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public Initializable replaceSceneContent(String fxml) throws Exception {
            FXMLLoader loader = new FXMLLoader();
            InputStream in = Main.class.getResourceAsStream(fxml);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setLocation(Main.class.getResource(fxml));
            AnchorPane page;
            try {
                page = loader.load(in);
            } finally {
                in.close();
            }
            Scene scene = new Scene(page, 600, 600);
            currentStage.setScene(scene);
            currentStage.sizeToScene();
            return (Initializable) loader.getController();
        }
    }
}
