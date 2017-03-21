package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.engines.RSAEngine;
import main.engines.WindowsHandler;

public class Main extends Application {

    // On stocke globalement la clé publique du server afin de ne pas surcharger les appels API
    // Init : au démarrage.
    public static RSAEngine public_key_server;

    @Override
    public void start(Stage primaryStage) throws Exception{
        WindowsHandler windowsManager = new WindowsHandler(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
