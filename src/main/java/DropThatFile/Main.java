package DropThatFile;

import DropThatFile.engines.windowsManager.forms.LoginForm;
import javafx.application.Application;
import javafx.stage.Stage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
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
        //Logger log = LogManagement.getInstanceLogger(this);
        //PublicKey test = GlobalVariables.public_key_server;
        //XMLReader xml = XMLReader.Instance();
        //System.out.println(xml.get(XMLFields.URL_API));

        // Uncomment to set a Key Store for RSA encryption/decryption
        //KeyPair userKeyPair = KeyStoreFactory.getKeyPairFromKeyStore(password);

        //region RSA test
        //GlobalVariables.currentUser = new User(1, "nicolas.demoncourt@gmail.com", "aaaa", "Nicolas", "Cornu", new Date(), "opopo");
        //KeyStoreFactory.setKeyPairToKeyStore("aaaa");
        /*try{
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
            //log.error("Erreur en lors de l'encryptage RSA.\nMessage : \n" + ex.getMessage());
        }*/
        //endregion

        // API tests
        //System.out.println(RSAEngine.Instance().encrypt("{\"fname\":\"Olivier\", \"lname\":\"Lefebvre\", \"mail\":\"olivier.lefebvre@akeonet.com\", \"password\":\"eeee\", \"lastlogin\":\"2017-05-28\"}", GlobalVariables.public_key_server));
        //System.out.println(RSAEngine.Instance().encrypt("{\"name\":\"Group 10\"}", GlobalVariables.public_key_server));

        // Entry point of the application
        LoginForm startForm = new LoginForm(primaryStage);
        startForm.showForm();
    }
}
