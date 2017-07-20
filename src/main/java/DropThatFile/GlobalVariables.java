package DropThatFile;


import DropThatFile.engines.APIData.APIModels.APIConfig;
import DropThatFile.engines.KeyStoreFactory;
import DropThatFile.models.User;
import org.bouncycastle.util.encoders.Base64;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by Nicol on 22/03/2017.
 */
public class GlobalVariables {

    // On stocke globalement la clé publique du serveur afin de ne pas surcharger les appels API
    // Init : au démarrage.
    public static PublicKey public_key_server = null;

     static {
         if(public_key_server == null){
             String key = APIConfig.Instance().getPublicKeyServer();
             public_key_server = KeyStoreFactory.getPublicKeyFromString(key);
         }
     }

    public static User currentUser;

    public static String outputZipPath = System.getenv("APPDATA") + "\\DropThatFile\\tmpfiles\\";
}
