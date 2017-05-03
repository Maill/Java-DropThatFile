package DropThatFile.engines.windowsManager.userForms;

import DropThatFile.engines.windowsManager.WindowsHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Nicol on 15/04/2017.
 */
public class MyParametersForm extends WindowsHandler {
    public MyParametersForm(Stage primaryStage) throws Exception{
        super(primaryStage);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ihm/myParameters.fxml"));
        jfxStage.setTitle("DropThatFile");
        jfxStage.setScene(new Scene(root));
    }

    public void show(){
        jfxStage.show();
    }

    /*public User getLoggedUser() { return loggedUser; }

    public boolean userLogging(String userId, String password) throws Exception {
        if (Authenticator.validate(userId, password)) {
            loggedUser = User2.of(userId);
            goToProfile();
            return true;
        } else {
            return false;
        }
    }*/
}
