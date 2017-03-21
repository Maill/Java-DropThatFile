package main.engines;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Nicolas on 02/03/2017.
 */

public class WindowsHandler {

    private Stage currentWindow;
    //private Stage windowToLoad;

    public Stage getCurrentWindow(){
        return currentWindow;
    }

    public WindowsHandler(Stage primaryStage)throws Exception{
        currentWindow = primaryStage;
        EntryPoint();
    }

    public void EntryPoint() throws Exception{
        // La fenêtre qui s'ouvre en premier doit être la fenêtre de connexion
        loginForm.setAndShowWindow(currentWindow, this);
    }

    public static class loginForm{

        public static void setWindow(Stage window, WindowsHandler handledWindow) throws Exception{
            Parent root = FXMLLoader.load(handledWindow.getClass().getResource("../ihm/loginForm.fxml"));
            window.setTitle("Hello World");
            window.setScene(new Scene(root, 300, 275));

        }

        public static void showWindow(Stage window){
            window.show();
        }

        public static void setAndShowWindow(Stage window, WindowsHandler handledWindow) throws Exception{
            setWindow(window, handledWindow);
            showWindow(window);
        }
    }
}
