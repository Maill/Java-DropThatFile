package DropThatFile.engines;

import DropThatFile.models.User2;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Nicolas on 02/03/2017.
 */

public class WindowsHandler {

    private User2 loggedUser;
    private Object currentWindow; // Type Object car on ne connait pas précisement le type de fenêtre active.

    public Object getCurrentWindow() {
        return currentWindow;
    }

    // Initialisation de la fenetre d'ouverture au moment de lancement de l'application
    // Ici, on veut afficher la fenetre de connexion, alors on crée un objet de type : loginForm
    // Mais on peut très bien changer de type de fenetre, il suffit de créer un objet de la fenetre correspondante
    // Puis on appelle la fonction qui lance cette fenetre
    public void Init(Stage primaryStage)throws Exception{
        currentWindow = new WindowsHandler.loginForm(primaryStage);
        EntryPoint();
    }

    //Fonction qui permet d'afficher la fenetre de départ.
    public void EntryPoint() throws Exception{
        // La fenêtre qui s'ouvre en premier doit être la fenêtre de connexion (logique :p)
        ((loginForm) currentWindow).setAndShowWindow();
    }


    // Classe interne : classe qui va permettre de gerer tout ce qui en rapport avec le type de fenetre
    public class loginForm{

        // Attribut qui contient le "Stage" de javaFX
        private Stage currentStage;

        public Stage getCurrentStage() {
            return currentStage;
        }

        // Constructeur qui prends en paramètre le "Stage" javaFX
        public loginForm(Stage primaryStage){
            currentStage = primaryStage;
        }

        // Préparation de la fenêtre
        public void setWindow() throws Exception{
            // On récupère le fxml et on le charge
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ihm/login.fxml"));
            // On défini un titre de fenêtre
            currentStage.setTitle("DropThatFile");
            // On encapsule le fxml dans le "Stage"
            currentStage.setScene(new Scene(root));
        }

        // On affiche la fenêtres
        public void showWindow(){
            currentStage.show();
        }

        public void setAndShowWindow() throws Exception{
            setWindow();
            showWindow();
        }
    }
}
