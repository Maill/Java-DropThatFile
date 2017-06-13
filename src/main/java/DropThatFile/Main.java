package DropThatFile;

import DropThatFile.engines.KeyStoreFactory;
import DropThatFile.engines.LogManagement;
import DropThatFile.engines.RSAEngine;
import DropThatFile.engines.XMLReader;
import DropThatFile.engines.windowsManager.forms.LoginForm;
import DropThatFile.models.Group;
import DropThatFile.models.User;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import java.security.KeyPair;
import java.security.Security;
import java.util.Date;
import java.util.UUID;

public class Main extends Application{

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) {
        Application.launch(Main.class, (String[])null);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Logger log = LogManagement.getInstanceLogger(this);

        XMLReader xml = XMLReader.Instance();
        // Decomment to set a Key Store for RSA encryption/decryption
        //KeyStoreFactory.setKeyPairToKeyStore("password");
        KeyPair pair = KeyStoreFactory.getKeyPairFromKeyStore("password");

        //region RSA test
        GlobalVariables.currentUser = new User(1, "test@test.com", "testtest", pair, "Prenom", "Nom",
                new Date(), "0645464534", new Group(1, "Groupe 1", GlobalVariables.public_key_server), UUID.randomUUID());
        try{
            RSAEngine rsaEngine = RSAEngine.Instance();
            //System.out.println("Clé privée : " + Base64.toBase64String(pair.getPrivate().getEncoded()));
            //System.out.println("Clé publique : " + Base64.toBase64String(pair.getPublic().getEncoded()));
            //System.out.println("----------------------------------------Crypting----------------------------------------------");
            //System.out.println("----------------------------------------------------------------------------------------------");
            String message = "Ceci est une phrase de texte décodée.";
            String cryptedMessage = rsaEngine.encrypt(message);
            //System.out.println("Message : " + message + "\n" + "Message encrypté : " + cryptedMessage);
            //System.out.println("----------------------------------------------------------------------------------------------");
            //System.out.println("Message encrypté : " + cryptedMessage + "\n" + "Message en clair : " + rsaEngine.decrypt(cryptedMessage));
            //System.out.println("----------------------------------------------------------------------------------------------");
            String signedMessage = rsaEngine.sign(cryptedMessage);
            boolean isVerified = rsaEngine.verify(cryptedMessage, signedMessage, pair.getPublic());
            //System.out.println("Signature : " + rsaEngine.sign(cryptedMessage) + "\n" + "Signature conforme ? : " + isVerified);
            //System.out.println("----------------------------------------------------------------------------------------------");
            //System.out.println("Clé publique du serveur : " + Base64.toBase64String(GlobalVariables.public_key_server.getEncoded()));
        }
        catch(Exception ex){
            log.error("Erreur en lors de l'encryptage RSA.\nMessage : \n" + ex.getMessage());
        }
        //endregion

        // Entry point of the application
        LoginForm startForm = new LoginForm(primaryStage);
        startForm.showForm();
    }
}
