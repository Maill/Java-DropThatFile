package DropThatFile;

import DropThatFile.engines.WindowsHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{
        GlobalVariables.windowsManager.Init(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
