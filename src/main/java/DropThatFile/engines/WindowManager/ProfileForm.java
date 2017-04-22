package DropThatFile.engines.WindowManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Nicol on 15/04/2017.
 */
public class ProfileForm extends WindowsHandler{


    public ProfileForm(Stage primaryStage) throws Exception{
        super(primaryStage);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ihm/profile.fxml"));
        jfxStage.setTitle("DropThatFile");
        jfxStage.setScene(new Scene(root));
    }

    public void show(){
        jfxStage.show();
    }
}
