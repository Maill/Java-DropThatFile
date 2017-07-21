package DropThatFile;

import DropThatFile.engines.APIData.APIModels.APIConfig;
import DropThatFile.engines.KeyStoreFactory;
import DropThatFile.models.Group;
import DropThatFile.models.User;

import java.security.PublicKey;
import java.util.ArrayList;

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

    public static ArrayList<Group> currentGroups = new ArrayList<>();

    // Groups of the current user
    public static ArrayList<String> groupPaths = new ArrayList<>();

    public static String appDataPath = System.getenv("APPDATA");

    public static String repositoriesMainPath = appDataPath.concat("\\DropThatFile\\repositories\\");

    public static String groupRepoMainPath = repositoriesMainPath.concat("groupFiles\\");

    public static String userRepoMainPath = repositoriesMainPath.concat("userFiles\\");

    public static String currentUserRepoPath;
}
