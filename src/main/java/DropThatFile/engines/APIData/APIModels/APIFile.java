package DropThatFile.engines.APIData.APIModels;

import DropThatFile.GlobalVariables;
import DropThatFile.engines.APIData.APIConnector;
import DropThatFile.engines.LogManagement;
import DropThatFile.engines.RSAEngine;
import DropThatFile.models.Group;
import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Nicol on 13/05/2017.
 */
public class APIFile extends APIConnector {
    private String route = this.baseURL + "/files/";

    private Logger log = LogManagement.getInstanceLogger(this);

    public static void getFilesUser(){
        //Recupère les fichier appartenant au User courrant
        //Remplis la treeview
    }

    public static void getFilesGroups(){
        //Récupère les fichier appartenant aux groupes dont l'utilisateur
        //courrant est un membre
    }

    public void addFileUser(String fileName, String password, String description){
        //ajout des informations du fichier lors de l'ajout d'un fichier
        try{
            String encryptedFileInfoJSON = RSAEngine.Instance().encrypt("{\"name\":\"" + fileName + "\", \"description\":\"" + description + "\"}", GlobalVariables.public_key_server);
            String encryptedFilePasswordJSON = RSAEngine.Instance().encrypt(password);
            List<NameValuePair> postContentFileInfo = this.buildPOSTList(null, "dataFile", encryptedFileInfoJSON);
            List<NameValuePair> postContentFilePassword = this.buildPOSTList(null, "passwordFile", encryptedFilePasswordJSON);
            JSONObject responseFileInfo = this.readFromUrl(this.route + "addFile", postContentFileInfo);
            JSONObject responseFilePassword = this.readFromUrl(this.route + "addFile", postContentFilePassword);
        }catch (Exception ex){
            log.error(String.format("Error on APIFile on addFile method\nMessage:\n%s\nStacktrace:\n%s", ex.getMessage(), Arrays.toString(ex.getStackTrace())));
        }
    }

    public static void addFileGroup(Group[] groups){
        //Ajoute l'information du fichier dans la vue des groupes
    }

}
