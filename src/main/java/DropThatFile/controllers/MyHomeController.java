package DropThatFile.controllers;

import DropThatFile.engines.windowsManager.LoginForm;
import DropThatFile.engines.windowsManager.WindowsHandler;
import DropThatFile.engines.windowsManager.userForms.MyHomeForm;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Travail on 02/05/2017.
 */
public class MyHomeController extends AnchorPane implements Initializable {

    private WindowsHandler windowsHandler;

    private MyHomeForm application;

    public void setApp(MyHomeForm application){
        this.application = application;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
