package DropThatFile.controllers;

import DropThatFile.engines.windowsManager.userForms.HomeForm;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Travail on 02/05/2017.
 */
public class HomeController extends AnchorPane implements Initializable {
    private HomeForm application;

    public void setApp(HomeForm application){
        this.application = application;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void openLink() throws URISyntaxException, IOException {
        Desktop desktop = Desktop.getDesktop();
        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(new URI("www.google.com"));
        } else {
            // ...
        }
    }
}
