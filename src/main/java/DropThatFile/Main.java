package DropThatFile;

import DropThatFile.engines.APIData.APIConnector;
import DropThatFile.engines.KeyStoreFactory;
import DropThatFile.engines.RSAEngine;
import DropThatFile.engines.XMLReader;
import DropThatFile.engines.windowsManager.forms.LoginForm;

import javafx.application.Application;
import javafx.stage.Stage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import java.security.KeyPair;
import java.security.Security;

public class Main extends Application{

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) {
        Application.launch(Main.class, (String[])null);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        XMLReader xml = XMLReader.Instance();
        KeyPair pair = KeyStoreFactory.getKeyPairFromKeyStore("password");
        try{
            System.out.println("Clé privée : " + Base64.toBase64String(pair.getPrivate().getEncoded()));
            System.out.println("Clé publique : " + Base64.toBase64String(pair.getPublic().getEncoded()));
            System.out.println("----------------------------------------Crypting----------------------------------------------");
            System.out.println("----------------------------------------------------------------------------------------------");
            String message = "Je veux aller jouer a Rocket League.";
            String cryptedMessage = RSAEngine.encrypt(message, pair.getPublic());
            System.out.println("Message : " + message + "\n" + "Message crypté : " + cryptedMessage);
            System.out.println("----------------------------------------------------------------------------------------------");
            System.out.println("Message crypté : " + cryptedMessage + "\n" + "Message en clair : " + RSAEngine.decrypt(cryptedMessage, pair.getPrivate()));
            System.out.println("----------------------------------------------------------------------------------------------");
            String signedMessage = RSAEngine.sign(cryptedMessage, pair.getPrivate());
            boolean isVerified = RSAEngine.verify(cryptedMessage, signedMessage, pair.getPublic());
            System.out.println("Signature : " + RSAEngine.sign(cryptedMessage, pair.getPrivate()) + "\n" + "Signature conforme ? : " + isVerified);
            System.out.println("----------------------------------------------------------------------------------------------");
            System.out.println("Clé publique du serveur : " + Base64.toBase64String(GlobalVariables.public_key_server.getEncoded()));
        }
        catch(Exception e){
            e.printStackTrace();
        }

        LoginForm startForm = new LoginForm(primaryStage);
        startForm.showForm();
    }
}
