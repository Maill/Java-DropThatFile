package DropThatFile.controllers.profileForm;

import DropThatFile.engines.WindowsHandler;
import DropThatFile.models.User2;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController extends AnchorPane implements Initializable {

    @FXML
    private TextField userEmail;
    @FXML
    private TextField username;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField postalAddress;
    @FXML
    private Hyperlink logout;
    @FXML 
    private Button proceed;
    @FXML 
    private Label profileMessage;
    
    private WindowsHandler.loginForm application;

    public void setApp(WindowsHandler.loginForm application){
        this.application = application;
        User2 loggedUser = application.getLoggedUser();
        userEmail.setText(loggedUser.getId());
        username.setText(loggedUser.getUsername());
        phoneNumber.setText(loggedUser.getPhoneNumber());
        setMessage(profileMessage, null, 0,null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * OnClick on the "logout" hyperlink.
     * @param event
     */
    public void processLogout(ActionEvent event) {
        if (application == null){
            return;
        }
        application.userLogout();
    }

    /**
     * OnClick on the "Continue" button.yh
     * @param event
     */
    public void saveProfile(ActionEvent event) {
        if (application == null){
            animateMessage();
            return;
        }
        User2 loggedUser = application.getLoggedUser();
        loggedUser.setUsername(username.getText());
        loggedUser.setPhoneNumber(phoneNumber.getText());
        loggedUser.setPostalAddress(postalAddress.getText());
        setMessage(profileMessage, null, 0,null);
        animateMessage();



        //TODO : save user in database



    }

    /**
     * OnClick on the "Reset" button.
     * @param event
     */
    public void resetProfile(ActionEvent event){
        if (application == null){
            return;
        }
        username.setText("");
        username.setPromptText("Username...");
        phoneNumber.setText("");
        phoneNumber.setPromptText("Phone number...");
        postalAddress.setText("");
        postalAddress.setPromptText("Postal address...");
        setMessage(profileMessage, null, 0,null);
    }

    /**
     * Set a customizable message
     * @param label The label to alter
     * @param colour The colour to set. Default = black.
     * @param opacity The text to set
     * @param text The text to set
     */
    private void setMessage(Label label, String colour, int opacity, String text){
        if(colour == null && text == null)
            return;
        label.setOpacity(opacity == 1 ? 1 : 0);
        label.setText(text);
        label.setTextFill(Color.web(colour == null ? "black" : colour));
    }

    private void animateMessage() {
        FadeTransition ft = new FadeTransition(Duration.millis(1000), profileMessage);
        ft.setFromValue(0.0);
        ft.setToValue(1);
        ft.play();
    }
}
