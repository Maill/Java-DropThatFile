package DropThatFile.controllers.loginForm;

import DropThatFile.engines.WindowsHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends AnchorPane implements Initializable {

    @FXML
    TextField userEmail;
    @FXML
    PasswordField password;
    @FXML
    Button login;
    @FXML
    Label loginMessage;

    private WindowsHandler.loginForm application;
    
    public void setApp(WindowsHandler.loginForm application){
        this.application = application;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setMessage(loginMessage,"black",1,"Welcome on DropThatFile. Please enter your email and your password.");
        userEmail.setPromptText("Email...");
        password.setPromptText("Password...");
        
    }

    /**
     * OnClick on the "Login" button.
     * @param event
     */
    public void loginVerification(ActionEvent event) throws Exception {
        if (!userEmail.getText().matches("^(?:(?:[a-z0-9]+\\.[a-z0-9]+)|(?:[a-z0-9]+))@(?:[a-z0-9]+(?:\\.[a-z0-9]+)+)$") || userEmail.getText() == null){
            setMessage(loginMessage,"red",1,"Your email address is invalid.");
        } else if(password.getText() == null){
            setMessage(loginMessage,"red",1,"Your password is invalid.");
        } else if(password.getText().contains(" ")) {
            setMessage(loginMessage,"red",1,"Your password must not contains any whitespace.");
        } else if (password.getText().length() < 8) {
            setMessage(loginMessage,"red",1,"Your password must contains at least 8 characters.");
        } else if (!application.userLogging(userEmail.getText(), password.getText())) {
            setMessage(loginMessage,"red", 1,"Your email or your password is unknown.");
        }
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

    /**
     * OnClick on the "Exit" button.
     * @param event
     */
    public void exitApplication(ActionEvent event){
        // WARNING : Close every connection or every instance before exiting the application !!!
        System.exit(0);
    }
}
