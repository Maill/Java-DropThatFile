package DropThatFile;

import DropThatFile.engines.KeyStoreFactory;
import DropThatFile.engines.XMLReader;

import DropThatFile.engines.windowsManager.WindowsHandler;
import DropThatFile.engines.windowsManager.forms.LoginForm;
import javafx.application.Application;
import javafx.stage.Stage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
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
        // Decomment to set a Key Store for RSA encryption/decryption
        //KeyStoreFactory.setKeyPairToKeyStore("password");
        KeyPair pair = KeyStoreFactory.getKeyPairFromKeyStore("password");

        //region RSA test
        /*try{
            System.out.println("Clé privée : " + Base64.toBase64String(pair.getPrivate().getEncoded()));
            System.out.println("Clé publique : " + Base64.toBase64String(pair.getPublic().getEncoded()));
            System.out.println("----------------------------------------Crypting----------------------------------------------");
            System.out.println("----------------------------------------------------------------------------------------------");
            String message = "Ceci est une phrase de texte décodée.";
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
        }*/
        //endregion

        // Entry point of the application
        LoginForm startForm = new LoginForm(primaryStage);
        startForm.showForm();

        // TODO: Entry point of the application v2
        //WindowsHandler windowsHandler = new WindowsHandler(primaryStage);
        //windowsHandler.setAuthentication();
    }
}
