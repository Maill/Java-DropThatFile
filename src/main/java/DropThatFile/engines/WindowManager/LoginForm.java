package DropThatFile.engines.WindowManager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Nicol on 15/04/2017.
 */
public class LoginForm extends WindowsHandler{

    private final org.apache.log4j.Logger log = logManagement.Instance(this);

    public LoginForm(Stage primaryStage) throws Exception{
        super(primaryStage);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ihm/login.fxml"));
        jfxStage.setTitle("DropThatFile");
        jfxStage.setScene(new Scene(root));
        jfxStage.setResizable(false);
    }

    public void show(){
        jfxStage.show();
    }

    public boolean userLogging(String userId, String password) throws Exception {
        return true;
    }

    public void goToProfile() {
        try {
            Stage profileStage = new Stage();
            ProfileForm profile = new ProfileForm(profileStage);
            profile.show();
        } catch (Exception ex) {
            log.error("Unable to load Profile Form");
        }
    }
}
