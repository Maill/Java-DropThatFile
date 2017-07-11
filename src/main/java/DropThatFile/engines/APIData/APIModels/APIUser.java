package DropThatFile.engines.APIData.APIModels;

import DropThatFile.GlobalVariables;
import DropThatFile.engines.APIData.APIConnector;
import DropThatFile.engines.KeyStoreFactory;
import DropThatFile.engines.LogManagement;
import DropThatFile.engines.RSAEngine;
import DropThatFile.models.Group;
import DropThatFile.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.docx4j.dml.CTGraphicalObjectFrameLocking;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.KeyPair;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Nicol on 13/05/2017.
 */
public class APIUser extends APIConnector {

    private String route = this.baseURL + "/accounts/";

    private Logger log = LogManagement.getInstanceLogger(this);

    private APIUser instance = null;

    public boolean login(String formattedEncryptedCredentials, String password){
        boolean returnVal;
        try{
            JSONObject response = this.readFromUrl(this.route + "login/" + formattedEncryptedCredentials);
            if(response.get("token").toString() == null){
                return false;
            }
            returnVal = getUserInfo(response.get("token").toString(), formattedEncryptedCredentials, password);
        }catch (Exception ex){
            log.error(String.format("Error on APIUser on login method\nMessage:\n%s\nStacktrace:\n%s", ex.getMessage(), ex.getStackTrace().toString()));
            return false;
        }
        return returnVal;
    }

    public boolean getUserInfo(String tokenJWT, String formattedEncryptedCredentials, String password){
        try{
            JSONObject response = this.readFromUrl(this.route + "get/" + formattedEncryptedCredentials);
            if(response.get("id") == null && response.get("mail") == null){
                return false;
            }
            GlobalVariables.currentUser = new User(Integer.parseInt(response.get("id").toString()), response.get("email").toString(), response.get("password").toString(),
                    KeyStoreFactory.getKeyPairFromKeyStore(password), response.get("fname").toString(), response.get("lname").toString(),
                    getDateFrommString(response.get("lastlogin").toString()), response.get("phonenumber").toString(), createGroupFromJSON(response.getJSONArray("members")), tokenJWT);
        } catch(Exception ex){
            log.error(String.format("Error on APIUser on login method\nMessage:\n%s\nStacktrace:\n%s", ex.getMessage(), ex.getStackTrace().toString()));
            return false;
        }
        return true;
    }

    private Group[] createGroupFromJSON(JSONArray members){
        Group[] ret = null;

        return ret;
    }

    private java.util.Date getDateFrommString(String date) throws Exception{
        DateFormat format = new SimpleDateFormat("dd/mm/yyyy", Locale.FRANCE);
        return format.parse(date);
    }

    public boolean saveProfile(){
        ObjectMapper mapper = new ObjectMapper();
        try{
            JSONObject response = this.readFromUrl(this.route + "saveprofile/" + RSAEngine.Instance().encrypt(mapper.writeValueAsString(GlobalVariables.currentUser), GlobalVariables.public_key_server));
            if(response.get("status").toString() == null){
                return false;
            }
        }catch (Exception ex){
            log.error(String.format("Error on APIUser on login method\nMessage:\n%s\nStacktrace:\n%s", ex.getMessage(), ex.getStackTrace().toString()));
            return false;
        }
        return true;
    }

    public APIUser Instance(){
        if(this.instance == null){
            this.instance = new APIUser();
            return this.instance;
        } else {
            return this.instance;
        }
    }
}
