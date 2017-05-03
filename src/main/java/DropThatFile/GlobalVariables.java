package DropThatFile;

import DropThatFile.engines.RSAEngine;

/**
 * Created by Nicol on 22/03/2017.
 */
public interface GlobalVariables {


    // On stocke globalement la clé publique du serveur afin de ne pas surcharger les appels API
    // Init : au démarrage.
    RSAEngine public_key_server = null; // A assigner par défaut quand l'on pourra récupérer des choses dans la BDD
}
