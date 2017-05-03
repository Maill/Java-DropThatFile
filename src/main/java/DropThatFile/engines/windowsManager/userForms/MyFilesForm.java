package DropThatFile.engines.windowsManager.userForms;

import DropThatFile.engines.windowsManager.WindowsHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Nicol on 15/04/2017.
 */
public class MyFilesForm extends WindowsHandler {


    public MyFilesForm(Stage primaryStage) throws Exception{
        super(primaryStage);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ihm/myFiles.fxml"));
        jfxStage.setTitle("DropThatFile");
        jfxStage.setScene(new Scene(root));
    }

    public void show(){
        jfxStage.show();
    }
}