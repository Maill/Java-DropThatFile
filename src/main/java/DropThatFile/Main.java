package DropThatFile;

import DropThatFile.engines.WindowManager.LoginForm;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

    public static void main(String[] args) {
        Application.launch(Main.class, (String[])null);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        LoginForm startForm = new LoginForm(primaryStage);
        startForm.show();
    }
}
