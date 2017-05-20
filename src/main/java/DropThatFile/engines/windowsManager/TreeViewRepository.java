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
    // Create an ArrayList of TreeItems in which all data (TreeItems) will be displayed
    public ArrayList<TreeItem> getAll()
    {
        ArrayList<TreeItem> directories = new ArrayList<>();
        TreeItem myFiles = new TreeItem("My Files");
        TreeItem sharedFiles = new TreeItem("Shared Files");
        myFiles.getChildren().addAll(getMyFiles());
        sharedFiles.getChildren().addAll(getSharedFiles());

        directories.add(myFiles);
        directories.add(sharedFiles);

        return directories;
    }

    // Create an ArrayList of TreeItems
    private ArrayList<TreeItem> getMyFiles()
    {
        ArrayList<TreeItem> myFiles = new ArrayList<>();

        myFiles.add(new TreeItem<>("Documents"));
        myFiles.add(new TreeItem<>("Musiques"));
        myFiles.add(new TreeItem<>("Photos"));
        myFiles.add(new TreeItem<>("Vidéos"));

        return myFiles;
    }

    // Create an ArrayList of TreeItems
    private ArrayList<TreeItem> getSharedFiles()
    {
        ArrayList<TreeItem> myFiles = new ArrayList<>();

        myFiles.add(new TreeItem<>("Archives"));
        myFiles.add(new TreeItem<>("Temp"));
        return myFiles;
    }
}
