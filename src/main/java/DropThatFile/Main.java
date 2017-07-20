package DropThatFile;

import DropThatFile.engines.APIData.APIModels.APIFile;
import DropThatFile.engines.APIData.APIModels.APIUser;
import DropThatFile.engines.FilesJobs;
import DropThatFile.engines.RSAEngine;
import DropThatFile.engines.windowsManager.forms.LoginForm;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.Security;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.CompletableFuture;

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

        ThreadGroup test = FilesJobs.Instance().retrieveFilesFromServer();
        while(test.activeCount() != 0){
            System.out.println("pas fini");
        }

        System.out.println("end");
        // Entry point of the application
        //LoginForm startForm = new LoginForm(primaryStage);
        //startForm.showForm();
    }
}
