package DropThatFile.engines.windowsManager;

import javafx.scene.control.TreeItem;

import java.util.ArrayList;

/**
 * Created by Olivier on 20/05/2017.
 */
public class TreeViewRepository {
    public TreeViewRepository()
    {
    }

    //TODO : Récupérer données (fichiers) depuis la BDD
    // This method creates an ArrayList of TreeItems in which all data (TreeItems) will be displayed
    public ArrayList<TreeItem> getAll()
    {
        ArrayList<TreeItem> directories = new ArrayList<>();
        directories.add(new TreeItem<>("Documents"));
        directories.add(new TreeItem<>("Musiques"));
        directories.add(new TreeItem<>("Photos"));
        directories.add(new TreeItem<>("Vidéos"));

        return directories;
    }
}
