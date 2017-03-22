package DropThatFile.engines;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Nicolas on 02/03/2017.
 */

public class WindowsHandler {
    private Object currentWindow; // Type Object car on ne sais pas précisément le type de fenetre active.

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

    class loginForm{

        private Stage currentStage;

        public Stage getCurrentStage() {
            return currentStage;
        }

        public loginForm(Stage primaryStage){
            currentStage = primaryStage;
        }

        public void setWindow() throws Exception{
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ihm/loginForm.fxml"));
            currentStage.setTitle("Hello World");
            currentStage.setScene(new Scene(root, 300, 275));
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
