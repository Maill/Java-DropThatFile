package DropThatFile.engines.APIData.APIModels;

import DropThatFile.engines.APIData.APIConnector;

/**
 * Created by Nicol on 13/05/2017.
 */
public class APIUser extends APIConnector {

    public static void login(){
        //Connexion API et récupération du token
        //Puis appel à getUserInfo() pour construire le User
    }

    public static void getUserInfo(String tokenJWT){
        //appel de la route API de récupération User
        //L'API décodera le token pour récuperer le UserID
        //Récupération et création de User et stockage du User dans Variable globales
    }

    public static void saveProfile(){
        //sauvegarde du profil
    }

}
