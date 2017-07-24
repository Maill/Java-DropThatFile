package DropThatFile;

import DropThatFile.engines.windowsManager.forms.LoginForm;
import javafx.application.Application;
import javafx.stage.Stage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class Main extends Application{

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) {
        Application.launch(Main.class, (String[])null);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //APIUser.Instance().login("nicolas.demoncourt@gmail.com", "aaaa");
        //FilesJobs.Instance().SendGroupFileToServer(new File("C:\\Users\\Nicol\\AppData\\Roaming\\DropThatFile\\repositories\\groupfiles\\Groupe 2\\888.txt"));

        // Entry point of the application
        LoginForm startForm = new LoginForm(primaryStage);
        startForm.showForm();
    }
}
