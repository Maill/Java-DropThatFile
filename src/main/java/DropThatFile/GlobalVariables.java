package DropThatFile;

import DropThatFile.engines.RSAEngine;
import DropThatFile.engines.WindowsHandler;

import java.security.KeyPair;

/**
 * Created by Nicol on 22/03/2017.
 */
public abstract class GlobalVariables {

    static WindowsHandler windowsManager = new WindowsHandler();

    // On stocke globalement la clé publique du serveur afin de ne pas surcharger les appels API
    // Init : au démarrage.
    RSAEngine public_key_server = null; // A assigner par défaut quand l'on pourra récupérer des choses dans la BDD
    //Generation of public/private key pair
    static {
        try {
            final KeyPair pair = RSAEngine.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
