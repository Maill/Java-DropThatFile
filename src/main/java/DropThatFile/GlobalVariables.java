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
    // Public key global storage
    public static PublicKey public_key_server = null;

    static {
        if(public_key_server == null){
            String key = APIConfig.Instance().getPublicKeyServer();
            public_key_server = KeyStoreFactory.getPublicKeyFromString(key);
        }
    }

    public static User currentUser;

    public static ArrayList<Group> currentGroups = new ArrayList<>();

    public static String appDataPath = System.getenv("APPDATA");

    public static String repositoriesMainPath = appDataPath.concat("\\DropThatFile\\repositories\\");

    public static String groupRepoMainPath = repositoriesMainPath.concat("groupfiles\\");

    public static String userRepoMainPath = repositoriesMainPath.concat("userfiles\\");

    public static String currentUserRepoPath;
}
