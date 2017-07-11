package DropThatFile;


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

     static {
        String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhEcDijjCogrektOaPKb6+EqAhGrmSCbSFVhU24TKLsFpfDXAyYllhC3RZeUepkvmR2HniVwb7ytUZyB6vsun/nQXAbLYaa2lx+i6h8inG4xMOBHBeynwlR3t0rErIwKVwOYSOqgXrWtFBSEZzIZIcpQInvMXma9fSvVjEZj9JY7Ejn4E3HBq9CFArqyE3kLGK178MM+qzNBtBJth3PLKEcj47QuxxB3m1in4GjvyL1B5hajZemyiFvHDxddPsUwRNHJCz9tg+Ipk4K2ivoA+mcBYePbLYb4z76aUo5R/qX6a9mLEApmCfdm1bo6DOP/K+pyeUXD0m/cwNk/DYmTuHwIDAQAB";
        byte[] publicBytes = Base64.decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
         KeyFactory keyFactory = null;
         try {
             keyFactory = KeyFactory.getInstance("RSA");
         } catch (Exception e) {
             e.printStackTrace();
         }
         try {
             public_key_server = keyFactory.generatePublic(keySpec);
         } catch (InvalidKeySpecException e) {
             e.printStackTrace();
         }
     }
    // On stocke globalement la clé publique du serveur afin de ne pas surcharger les appels API
    // Init : au démarrage.
    public static PublicKey public_key_server;

    public static User currentUser;

    public static String outputZipPath = System.getenv("APPDATA") + "\\DropThatFile\\tmpfiles";
}
