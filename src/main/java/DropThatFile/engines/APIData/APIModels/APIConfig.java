package DropThatFile.engines.APIData.APIModels;

import DropThatFile.GlobalVariables;
import DropThatFile.engines.APIData.APIConnector;
import DropThatFile.engines.LogManagement;
import DropThatFile.engines.RSAEngine;
import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Nicolas on 04/07/2017.
 */
public class APIConfig extends APIConnector {

    private String route = this.baseURL + "/configuration/";

    private Logger log = LogManagement.getInstanceLogger(this);

    private static APIConfig instance = null;

    /**
     * Get the RSA Public Key of the server
     * @return Public server key as string
     */
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

    /**
     * Get the FTP credentials from the server
     * @return FTP credentials
     */
    public JSONObject getFTPInformations(){
        JSONObject returnVal = null;
        try{
            String strPubKey = new String(Base64.encode(GlobalVariables.currentUser.getUserKeys().getPublic().getEncoded()));
            List<NameValuePair> postContents = this.buildPOSTList(null, "clientPubKey", "-----BEGIN PUBLIC KEY----- " + strPubKey + " -----END PUBLIC KEY-----");
            JSONObject response = this.readFromUrl(this.route + "getFTPCrendentials", postContents);
            if(response.get("ftpcredentials").toString() == null){
                return null;
            }
            returnVal = new JSONObject(RSAEngine.Instance().decrypt(response.getString("ftpcredentials")));
        }catch (Exception ex){
            log.error(String.format("Error on APIConfiguration on getFTPInformations method\nMessage:\n%s\nStacktrace:\n%s", ex.getMessage(), ex.getStackTrace().toString()));
            return null;
        }
        return returnVal;
    }

    /**
     * Create or return the instance of APIConfig
     * @return APIConfig instance
     */
    public static APIConfig Instance(){
        if(instance == null){
            instance = new APIConfig();
            return instance;
        } else {
            return instance;
        }
    }
}
