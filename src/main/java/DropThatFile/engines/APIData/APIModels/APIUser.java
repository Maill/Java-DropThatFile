package DropThatFile.engines.APIData.APIModels;

import DropThatFile.GlobalVariables;
import DropThatFile.engines.APIData.APIConnector;
import DropThatFile.engines.KeyStoreFactory;
import DropThatFile.engines.LogManagement;
import DropThatFile.engines.RSAEngine;
import DropThatFile.models.Group;
import DropThatFile.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.docx4j.dml.CTGraphicalObjectFrameLocking;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.io.File;
import java.security.KeyPair;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nicol on 13/05/2017.
 */
public class APIUser extends APIConnector {

    private String route = this.baseURL + "/accounts/";

    private Logger log = LogManagement.getInstanceLogger(this);

    private static APIUser instance = null;

    /**
     * Connect the user and build his environment
     * @param email User's email
     * @param password User's password
     */
    public boolean login(String email, String password){
        boolean returnVal;
        JSONObject toAPI = new JSONObject();
        toAPI.append("email", email).append("password", password);
        try{
            String jsonStringEncrypted = RSAEngine.Instance().encrypt(toAPI.toString(), GlobalVariables.public_key_server);
            List<NameValuePair> postContents = this.buildPOSTList(null, "credentials", jsonStringEncrypted);
            JSONObject response = this.readFromUrl(this.route + "login", postContents);
            if(response.get("token").toString() == null){
                return false;
            }
            returnVal = setUserInfo(response, password);
        }catch (Exception ex){
            log.error(String.format("Error on APIUser on login method\nMessage:\n%s\nStacktrace:\n%s", ex.getMessage(), ex.getStackTrace().toString()));
            return false;
        }
        return returnVal;
    }

    /**
     * Build the User's object and his data
     * @param response Data from API
     * @param password User's password
     */
    public boolean setUserInfo(JSONObject response, String password){
        try{
            JSONObject userObject = new JSONObject(response.get("user").toString());
            if(!new File("keystroredtf.jks").exists()){
                KeyStoreFactory.setKeyPairToKeyStore(password, userObject.get("lname").toString(), userObject.get("fname").toString());
            }
            KeyPair userKeyPair = KeyStoreFactory.getKeyPairFromKeyStore(password);


            GlobalVariables.currentUser = new User(
                    Integer.parseInt(userObject.get("id").toString()),
                    userObject.get("mail").toString(),
                    RSAEngine.Instance().encrypt(password, userKeyPair.getPublic()),
                    userKeyPair,
                    userObject.get("fname").toString(),
                    userObject.get("lname").toString(),
                    this.getDateFrommString(userObject.get("lastlogin").toString()),
                    response.get("token").toString()
            );

            GlobalVariables.currentUser.setIsMemberOf(APIGroup.Instance().getGroupsForUser());

            GlobalVariables.currentUser.setFiles(APIFile.Instance().getFilesUser());

            APIFile.Instance().getFilesGroups();

        } catch(Exception ex){
            log.error(String.format("Error on APIUser on login method\nMessage:\n%s\nStacktrace:\n%s", ex.getMessage(), ex.getStackTrace().toString()));
            return false;
        }
        return true;
    }

    /**
     * Create or return the instance of APIUser
     * @return APIUser instance
     */
    public static APIUser Instance(){
        if(instance == null){
            instance = new APIUser();
            return instance;
        } else {
            return instance;
        }
    }
}
