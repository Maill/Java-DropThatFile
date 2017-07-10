package DropThatFile.engines.APIData.APIModels;

import DropThatFile.engines.APIData.APIConnector;
import DropThatFile.models.Group;

/**
 * Created by Nicol on 13/05/2017.
 */
public class APIFile extends APIConnector {

    public static void getFilesUser(){
        //Recupère les fichier appartenant au User courrant
        //Remplis la treeview
    }

    public static void getFilesGroups(){
        //Récupère les fichier appartenant aux groupes dont l'utilisateur
        //courrant est un membre
    }

    public static void addFileUser(){
        //ajout des informations du fichier lors de l'ajout d'un fichier
    }

    public static void addFileGroup(Group[] groups){
        //Ajoute l'information du fichier dans la vue des groupes
    }

}
