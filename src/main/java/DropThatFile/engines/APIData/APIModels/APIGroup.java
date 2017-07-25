package DropThatFile.engines.APIData.APIModels;

import DropThatFile.GlobalVariables;
import DropThatFile.engines.APIData.APIConnector;
import DropThatFile.engines.KeyStoreFactory;
import DropThatFile.engines.LogManagement;
import DropThatFile.models.Group;
import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import net.arnx.wmf2svg.util.Base64;

import java.util.*;

/**
 * Created by Nicol on 13/05/2017.
 */
public class APIGroup extends APIConnector {

    private String route = this.baseURL + "/groups/";

    private Logger log = LogManagement.getInstanceLogger(this);

    private static APIGroup instance = null;

    /**
     * Build the user's groups
     * @return User's groups
     */
    public HashMap<Integer, Group> getGroupsForUser(){
        HashMap<Integer, Group> ret;
        try{
            JSONObject response = this.readFromUrl(this.route + "getUserGroups", null);
            if(response.get("result").toString() == null){
                return new HashMap<>();
            }

            ret = buildGroupsFromResponse(response);

        }catch (Exception ex){
            log.error(String.format("Error on APIGroup on getGroupsForUser method\nMessage:\n%s\nStacktrace:\n%s",
                    ex.getMessage(), Arrays.toString(ex.getStackTrace())));
            return new HashMap<>();
        }

        return ret;
    }

    /**
     * Build a list for body HTTP Request
     * @param response Group's data from API
     * @return Create group's data from API
     */
    private HashMap<Integer, Group> buildGroupsFromResponse(JSONObject response){
        // Get data
        JSONArray getResult = response.getJSONArray("result");
        JSONObject getMemberof = getResult.getJSONObject(0);
        JSONArray getList = getMemberof.getJSONArray("memberof");
        // Initializing the list
        HashMap<Integer, Group> ret = new HashMap<>();

        for(Object rawGroup : getList){
            JSONObject groupJSON = new JSONObject(rawGroup.toString());
            ret.put(Integer.parseInt(
                    groupJSON.get("id").toString()),
                    new Group(Integer.parseInt(groupJSON.get("id").toString()),
                        groupJSON.get("name").toString(),
                        KeyStoreFactory.getPublicKeyFromString(groupJSON.get("public_key").toString())
                    )
            );
        }

        return ret;
    }

    public String getPasswordForGroupArchive(String passwordFile, String nameGroup){
        String ret = null;
        try{
            List<NameValuePair> postContent = this.buildPOSTList(null, "nameGroup", nameGroup);
            postContent = this.buildPOSTList(postContent, "passwordFile", passwordFile);
            postContent = this.buildPOSTList(postContent, "keyUser", Base64.encode(GlobalVariables.currentUser.getUserKeys().getPublic().getEncoded()));
            JSONObject response = this.readFromUrl(this.route + "getPasswordForFile", postContent);
            ret = response.getString("password");
        }catch (Exception ex){
            log.error(String.format("Error on APIFile on addArchiveUser method\nMessage:\n%s\nStacktrace:\n%s", ex.getMessage(), Arrays.toString(ex.getStackTrace())));
        }
        return ret;
    }

    /**
     * Create or return the instance of APIGroup
     * @return APIGroup instance
     */
    public static APIGroup Instance(){
        if(instance == null){
            instance = new APIGroup();
            return instance;
        } else {
            return instance;
        }
    }

}
