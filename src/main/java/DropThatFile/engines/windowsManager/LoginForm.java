package DropThatFile.engines.windowsManager;

import DropThatFile.engines.LogManagement;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Nicol on 15/04/2017.
 */
public class LoginForm extends WindowsHandler{

    private final org.apache.log4j.Logger log = LogManagement.Instance(this);

    public LoginForm(Stage primaryStage) throws Exception{
        super(primaryStage);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ihm/login.fxml"));
        jfxStage.setTitle("DropThatFile");
        jfxStage.setScene(new Scene(root));
        jfxStage.setResizable(false);
    }

    public boolean userLogging(String userId, String password) throws Exception {
        return true;
    }
}
