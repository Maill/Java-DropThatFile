package DropThatFile.engines.windowsManager.forms;

import DropThatFile.engines.windowsManager.WindowsHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Nicol on 15/04/2017.
 */
public class HomeForm extends WindowsHandler {
    public HomeForm(Stage primaryStage) throws Exception{
        super(primaryStage);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ihm/home.fxml"));
        jfxStage.setTitle("DropThatFile");
        jfxStage.setScene(new Scene(root));
    }
}