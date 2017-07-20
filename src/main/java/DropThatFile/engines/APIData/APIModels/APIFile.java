package DropThatFile.engines.APIData.APIModels;

import DropThatFile.GlobalVariables;
import DropThatFile.engines.APIData.APIConnector;
import DropThatFile.engines.KeyStoreFactory;
import DropThatFile.engines.LogManagement;
import DropThatFile.engines.RSAEngine;
import DropThatFile.models.File;
import DropThatFile.models.Group;
import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.plutext.jaxb.svg11.G;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nicol on 13/05/2017.
 */
public class APIFile extends APIConnector {
    private String route = this.baseURL + "/files/";

    private Logger log = LogManagement.getInstanceLogger(this);

    private static APIFile instance = null;

    public HashMap<String, File> getFilesUser(){
        HashMap<String, File> ret = new HashMap<>();

        try{
            JSONObject response = this.readFromUrl(route + "getFilesOfUser", null);

            JSONArray getResult = response.getJSONArray("result");
            JSONObject getMemberof = getResult.getJSONObject(0);
            JSONArray getList = getMemberof.getJSONArray("userfiles");

            for(Object rawFile : getList){
                JSONObject fileJSON = new JSONObject(rawFile.toString());
                ret.put(fileJSON.get("name").toString() , new File(
                        Integer.parseInt(fileJSON.get("id").toString()),
                        fileJSON.get("name").toString(),
                        fileJSON.get("password").toString(),
                        this.getDateFrommString(fileJSON.getJSONObject("filesofaccount").get("createdAt").toString()),
                        fileJSON.get("description").toString()
                ));
            }

        }catch (Exception ex){
            log.error(String.format("Error on APIFile on getFilesUser method\nMessage:\n%s\nStacktrace:\n%s", ex.getMessage(), Arrays.toString(ex.getStackTrace())));
            return ret;
        }

        return ret;
    }

    public void getFilesGroups(){
        HashMap<String, File> files = new HashMap<>();

        try{
            HashMap<Integer, Group> groupsList = GlobalVariables.currentUser.getIsMemberOf();
            List<NameValuePair> postContent = this.buildPOSTList(null, "groupsList", RSAEngine.Instance().encrypt(groupsList.keySet().toString(), GlobalVariables.public_key_server));
            JSONObject response = this.readFromUrl(route + "getFilesOfGroups", postContent);

            JSONArray getResult = response.getJSONArray("result");

            for(Object groupFiles : getResult){
                JSONObject groupFileJSON = new JSONObject(groupFiles.toString());
                Group currentGroup = groupsList.get(Integer.parseInt(groupFileJSON.get("id").toString()));
                for(Object file : groupFileJSON.getJSONArray("groupfiles")){
                    JSONObject fileJSON = new JSONObject(file.toString());
                    files.put(fileJSON.get("name").toString() , new File(
                            Integer.parseInt(fileJSON.get("id").toString()),
                            fileJSON.get("name").toString(),
                            fileJSON.get("password").toString(),
                            this.getDateFrommString(fileJSON.getJSONObject("filesofgroup").get("createdAt").toString()),
                            fileJSON.get("description").toString()
                    ));
                }
                currentGroup.setSharedFiles(files);
                groupsList.put(currentGroup.getId(), currentGroup);
                files.clear();
            }

            GlobalVariables.currentUser.setIsMemberOf(groupsList);

        }catch (Exception ex){
            log.error(String.format("Error on APIFile on getFilesUser method\nMessage:\n%s\nStacktrace:\n%s", ex.getMessage(), Arrays.toString(ex.getStackTrace())));
        }
    }

    public void addFileUser(String fileName, String password, String description){
        //ajout des informations du fichier lors de l'ajout d'un fichier
        try{
            JSONObject toAPI = new JSONObject();
            toAPI.append("name", fileName).append("descrption", description);
            String encryptedFileInfoJSON = RSAEngine.Instance().encrypt(toAPI.toString(), GlobalVariables.public_key_server);
            String encryptedFilePasswordJSON = RSAEngine.Instance().encrypt(password);
            List<NameValuePair> postContent = this.buildPOSTList(null, "dataFile", encryptedFileInfoJSON);
            postContent = this.buildPOSTList(postContent, "passwordFile", encryptedFilePasswordJSON);
            JSONObject responseFile = this.readFromUrl(this.route + "addFile", postContent);
        }catch (Exception ex){
            log.error(String.format("Error on APIFile on addFile method\nMessage:\n%s\nStacktrace:\n%s", ex.getMessage(), Arrays.toString(ex.getStackTrace())));
        }
    }

    public void addFileGroup(Group group){
        //Ajoute l'information du fichier dans la vue des groupes
    }

    public static APIFile Instance(){
        if(instance == null){
            instance = new APIFile();
            return instance;
        } else {
            return instance;
        }
    }

}
