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

    public ArrayList<Group> getGroupsForUser(){
        ArrayList<Group> ret;
        try{
            JSONObject response = this.readFromUrl(this.route + "getUserGroups", null);
            if(response.get("result").toString() == null){
                return new ArrayList<>();
            }

            ret = buildGroupsFromResponse(response);

        }catch (Exception ex){
            log.error(String.format("Error on APIGroup on getGroupsForUser method\nMessage:\n%s\nStacktrace:\n%s", ex.getMessage(), ex.getStackTrace().toString()));
            return new ArrayList<>();
        }

        return ret;
    }

    private ArrayList<Group> buildGroupsFromResponse(JSONObject response){
        //Récupération des données
        JSONArray getResult = response.getJSONArray("result");
        JSONObject getMemberof = getResult.getJSONObject(0);
        JSONArray getList = getMemberof.getJSONArray("memberof");
        //Initialisation de la liste
        ArrayList<Group> ret = new ArrayList<>();

        for(Object rawGroup : getList){
            JSONObject groupJSON = new JSONObject(rawGroup.toString());
            ret.add(new Group(Integer.parseInt(groupJSON.get("id").toString()), groupJSON.get("name").toString(), KeyStoreFactory.getPublicKeyFromString(groupJSON.get("public_key").toString())));
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
