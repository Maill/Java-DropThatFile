package DropThatFile.engines.windowsManager.forms;

import DropThatFile.GlobalVariables;
import DropThatFile.engines.LogManagement;
import DropThatFile.engines.annotations.Level;
import DropThatFile.engines.annotations._Todo;
import DropThatFile.engines.windowsManager.WindowsHandler;
import DropThatFile.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

/**
 * Created by Nicol on 15/04/2017.
 */
public class LoginForm extends WindowsHandler {
    private final Logger log = LogManagement.getInstanceLogger(this);

    public LoginForm(Stage primaryStage) throws Exception{
        super(primaryStage);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ihm/login.fxml"));
        jfxStage.setTitle("DropThatFile");
        jfxStage.setScene(new Scene(root));
        jfxStage.setResizable(false);
    }
}
