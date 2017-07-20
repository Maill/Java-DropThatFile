package DropThatFile.engines.APIData.APIModels;

import DropThatFile.engines.APIData.APIConnector;
import DropThatFile.engines.KeyStoreFactory;
import DropThatFile.engines.LogManagement;
import DropThatFile.models.Group;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by Nicol on 13/05/2017.
 */
public class APIGroup extends APIConnector {

    private String route = this.baseURL + "/groups/";

    private Logger log = LogManagement.getInstanceLogger(this);

    private static APIGroup instance = null;

    public HashMap<Integer, Group>  getGroupsForUser(){
        HashMap<Integer, Group> ret;
        try{
            JSONObject response = this.readFromUrl(this.route + "getUserGroups", null);
            if(response.get("result").toString() == null){
                return new HashMap<Integer, Group> ();
            }

            ret = buildGroupsFromResponse(response);

        }catch (Exception ex){
            log.error(String.format("Error on APIGroup on getGroupsForUser method\nMessage:\n%s\nStacktrace:\n%s", ex.getMessage(), ex.getStackTrace().toString()));
            return new HashMap<Integer, Group>();
        }

        return ret;
    }

    private HashMap<Integer, Group> buildGroupsFromResponse(JSONObject response){
        //Récupération des données
        JSONArray getResult = response.getJSONArray("result");
        JSONObject getMemberof = getResult.getJSONObject(0);
        JSONArray getList = getMemberof.getJSONArray("memberof");
        //Initialisation de la liste
        HashMap<Integer, Group> ret = new HashMap<>();

        for(Object rawGroup : getList){
            JSONObject groupJSON = new JSONObject(rawGroup.toString());
            ret.put(Integer.parseInt(groupJSON.get("id").toString()) ,new Group(Integer.parseInt(groupJSON.get("id").toString()), groupJSON.get("name").toString(), KeyStoreFactory.getPublicKeyFromString(groupJSON.get("public_key").toString())));
        }

        return ret;
    }

    public static APIGroup Instance(){
        if(instance == null){
            instance = new APIGroup();
            return instance;
        } else {
            return instance;
        }
    }

}
