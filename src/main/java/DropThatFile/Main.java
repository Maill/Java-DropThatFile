package DropThatFile;

import DropThatFile.engines.APIData.APIModels.APIUser;
import DropThatFile.engines.FilesJobs;
import DropThatFile.engines.windowsManager.forms.LoginForm;
import javafx.application.Application;
import javafx.stage.Stage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

import static DropThatFile.GlobalVariables.*;

public class Main extends Application{

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) {
        Application.launch(Main.class, (String[])null);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //region User
        APIUser.Instance().login("nicolas.demoncourt@gmail.com", "aaaa");
        String firstCharFName = currentUser.getfName().substring(0,1);
        String lName = currentUser.getlName();
        currentUserRepoPath = userRepoMainPath.concat(firstCharFName + lName + "\\");
        //endregion

        //region FTP
        FilesJobs.Instance().retrieveFilesFromServer();
        //endregion

        // Entry point of the application
        LoginForm startForm = new LoginForm(primaryStage);
        startForm.showForm();
    }
}
