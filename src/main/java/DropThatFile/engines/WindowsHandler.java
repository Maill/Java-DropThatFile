package DropThatFile.engines;

import DropThatFile.models.User2;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
            currentStage.setTitle("DropThatFile");
            currentStage.setScene(new Scene(root));
        }

        public void showWindow(){
            currentStage.show();
        }

        public void setAndShowWindow() throws Exception{
            setWindow();
            showWindow();
        }
    }
}
