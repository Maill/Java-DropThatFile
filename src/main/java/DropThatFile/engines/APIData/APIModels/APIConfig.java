package DropThatFile.engines.APIData.APIModels;

import DropThatFile.engines.APIData.APIConnector;
import DropThatFile.engines.LogManagement;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 * Created by Nicolas on 04/07/2017.
 */
public class APIConfig extends APIConnector {

    private String route = this.baseURL + "/configuration/";

    private Logger log = LogManagement.getInstanceLogger(this);

    private static APIConfig instance = null;

    public String getPublicKeyServer(){
        String returnVal;
        try{
            JSONObject response = this.readFromUrl(this.route + "getServerPublicKey", null);
            if(response.get("server_public_key").toString() == null){
                return null;
            }
            returnVal = response.get("server_public_key").toString();
        }catch (Exception ex){
            log.error(String.format("Error on APIConfiguration on getPublicKeyServer method\nMessage:\n%s\nStacktrace:\n%s", ex.getMessage(), ex.getStackTrace().toString()));
            return null;
        }
        return returnVal;
    }

    public static void getNETInformations(){
        //récupère les infos de connexion au serveur SAMBA distant
    }

    public static APIConfig Instance(){
        if(instance == null){
            instance = new APIConfig();
            return instance;
        } else {
            return instance;
        }
    }
}
