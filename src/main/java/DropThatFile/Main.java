package DropThatFile;

import DropThatFile.engines.APIData.APIModels.APIConfig;
import DropThatFile.engines.APIData.APIModels.APIFile;
import DropThatFile.engines.APIData.APIModels.APIUser;
import DropThatFile.engines.FilesJobs;
import DropThatFile.engines.windowsManager.forms.LoginForm;
import javafx.application.Application;
import javafx.stage.Stage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import java.io.File;
import java.security.Security;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application{

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) {
        Application.launch(Main.class, (String[])null);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        APIUser.Instance().login("nicolas.demoncourt@gmail.com", "aaaa");
        FilesJobs.Instance().SendGroupFileToServer(new File("C:\\Users\\Nicol\\AppData\\Roaming\\DropThatFile\\repositories\\groupfiles\\Groupe 2\\888.txt"));

        // Entry point of the application
        LoginForm startForm = new LoginForm(primaryStage);
        startForm.showForm();
    }
}
