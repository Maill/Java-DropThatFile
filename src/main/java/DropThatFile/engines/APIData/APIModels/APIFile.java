package DropThatFile.engines.APIData.APIModels;

import DropThatFile.GlobalVariables;
import DropThatFile.engines.APIData.APIConnector;
import DropThatFile.engines.KeyStoreFactory;
import DropThatFile.engines.LogManagement;
import DropThatFile.engines.RSAEngine;
import DropThatFile.models.File;
import DropThatFile.models.Group;
import com.sun.istack.internal.Nullable;
import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.plutext.jaxb.svg11.G;

import java.security.PublicKey;
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

    /**
     * Build the User's files
     * @return List of user's files
     */
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

    /**
     * Build the Group's files
     * @return List of groups's files
     */
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

    /**
     * Add a user's file on the database
     * @param fileName File's name
     * @param description Description of the file
     */
    public void addFileUser(String fileName, String description){
        //ajout des informations du fichier lors de l'ajout d'un fichier
        JSONObject toAPI = new JSONObject();
        toAPI.append("name", fileName).append("description", description);
        System.out.println(toAPI.toString());
        try{
            String encryptedFileInfoJSON = RSAEngine.Instance().encrypt(toAPI.toString(), GlobalVariables.public_key_server);
            List<NameValuePair> postContent = this.buildPOSTList(null, "dataFile", encryptedFileInfoJSON);
            this.readFromUrl(this.route + "accounts/addFile", postContent);
        }catch (Exception ex){
            log.error(String.format("Error on APIFile on addFile method\nMessage:\n%s\nStacktrace:\n%s", ex.getMessage(), Arrays.toString(ex.getStackTrace())));
        }
    }

    /**
     * Add a user archive on the database
     * @param fileName File's name
     * @param description Description of the file
     * @param password File's password
     */
    public void addArchiveUser(String fileName, String description, String password){
        //ajout des informations du fichier lors de l'ajout d'un fichier
        try{
            JSONObject toAPI = new JSONObject();
            toAPI.append("name", fileName).append("description", description);
            String encryptedFileInfoJSON = RSAEngine.Instance().encrypt(toAPI.toString(), GlobalVariables.public_key_server);
            List<NameValuePair> postContent = this.buildPOSTList(null, "dataFile", encryptedFileInfoJSON);
            String encryptedFilePasswordJSON = RSAEngine.Instance().encrypt(password);
            postContent = this.buildPOSTList(postContent, "passwordFile", encryptedFilePasswordJSON);
            this.readFromUrl(this.route + "accounts/addArchive", postContent);
        }catch (Exception ex){
            log.error(String.format("Error on APIFile on addArchiveUser method\nMessage:\n%s\nStacktrace:\n%s", ex.getMessage(), Arrays.toString(ex.getStackTrace())));
        }
    }

    /**
     * Add a group file on the database
     *
     */
    public void addFileGroup(String name, String description, String groupName){
        try{
            JSONObject toAPIDataFile = new JSONObject();
            toAPIDataFile.append("name", name).append("description", description);
            JSONObject toAPIDataGroup = new JSONObject();
            toAPIDataGroup.append("name", groupName);
            String encryptedFileInfoJSON = RSAEngine.Instance().encrypt(toAPIDataFile.toString(), GlobalVariables.public_key_server);
            String encryptedGroupInfoJSON = RSAEngine.Instance().encrypt(toAPIDataGroup.toString(), GlobalVariables.public_key_server);
            List<NameValuePair> postContent = this.buildPOSTList(null, "dataFile", encryptedFileInfoJSON);
            postContent = this.buildPOSTList(postContent, "dataGroup", encryptedGroupInfoJSON);
            this.readFromUrl(this.route + "groups/addFile", postContent);
        }catch (Exception ex){
            log.error(String.format("Error on APIFile on addFileGroup method\nMessage:\n%s\nStacktrace:\n%s", ex.getMessage(), Arrays.toString(ex.getStackTrace())));
        }
        //Ajoute l'information du fichier dans la vue des groupes
    }
    // Appel de la fonction
    /*APIFile.Instance().addFileGroup(
            file.getParent() + "\\" + file.getName(),
            HomeController.zipDescription
    );*/

    /**
     * Add a group archive on the database
     *
     */
    public void addArchiveGroup(String name, String description, String password, String groupName){
        try{
            JSONObject toAPIDataFile = new JSONObject();
            toAPIDataFile.append("name", name).append("description", description);
            JSONObject toAPIDataGroup = new JSONObject();
            toAPIDataGroup.append("name", groupName);
            String encryptedFileInfoJSON = RSAEngine.Instance().encrypt(toAPIDataFile.toString(), GlobalVariables.public_key_server);
            String encryptedGroupInfoJSON = RSAEngine.Instance().encrypt(toAPIDataGroup.toString(), GlobalVariables.public_key_server);
            List<NameValuePair> postContent = this.buildPOSTList(null, "dataFile", encryptedFileInfoJSON);
            postContent = this.buildPOSTList(postContent, "dataGroup", encryptedGroupInfoJSON);

            final PublicKey[] keyOfGroup = new PublicKey[1];
            GlobalVariables.currentUser.getIsMemberOf().forEach((k, v) -> {if(v.getName() == groupName){ keyOfGroup[0] = v.getPublic_key(); }});

            postContent = this.buildPOSTList(postContent, "passwordFile", RSAEngine.Instance().encrypt(password, keyOfGroup[0]));
            this.readFromUrl(this.route + "groups/addArchive", postContent);
        }catch (Exception ex){
            log.error(String.format("Error on APIFile on addFileGroup method\nMessage:\n%s\nStacktrace:\n%s", ex.getMessage(), Arrays.toString(ex.getStackTrace())));
        }
    }
    // Appel de la fonction
    /*APIFile.Instance().addArchiveGroup(
            file.getParent() + "\\" + file.getName(),
            HomeController.zipDescription,
            HomeController.zipPassword
    );*/

    public String getPasswordForGroupArchive(){
        return null;
    }

    /**
     * Create or return the instance of APIFile
     * @return APIFile instance
     */
    public static APIFile Instance(){
        if(instance == null){
            instance = new APIFile();
            return instance;
        } else {
            return instance;
        }
    }

}
