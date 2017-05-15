package DropThatFile.controllers;

import DropThatFile.engines.windowsManager.WindowsHandler;
import DropThatFile.engines.windowsManager.userForms.HomeForm;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Travail on 02/05/2017.
 */
public class HomeController extends AnchorPane implements Initializable {

    private WindowsHandler windowsHandler;

    private HomeForm application;

    public void setApp(HomeForm application){
        this.application = application;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
