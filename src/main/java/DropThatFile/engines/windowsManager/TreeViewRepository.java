package DropThatFile.engines.windowsManager;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**
 * Created by Olivier on 20/05/2017.
 */
public class TreeViewRepository {
    private Image folderImage = new Image(getClass().getResourceAsStream("/images/folder.png"));

    /**
     * Create an ArrayList of TreeItems in which all data (TreeItems) will be displayed
     */
    public ArrayList<TreeItem> getAll()
    {
        ArrayList<TreeItem> directories = new ArrayList<>();
        TreeItem myFiles = new TreeItem("My Files", new ImageView(folderImage));
        TreeItem sharedFiles = new TreeItem("Shared Files", new ImageView(folderImage));
        myFiles.getChildren().addAll(getMyFiles());
        sharedFiles.getChildren().addAll(getSharedFiles());

        directories.add(myFiles);
        directories.add(sharedFiles);

        return directories;
    }

    /**
     * Create an ArrayList of TreeItems
     * @return initial directories of the "My Files" directory
     */
    private ArrayList<TreeItem> getMyFiles()
    {
        ArrayList<TreeItem> myFiles = new ArrayList<>();

        myFiles.add(new TreeItem<>("Documents", new ImageView(folderImage)));
        myFiles.add(new TreeItem<>("Musiques", new ImageView(folderImage)));
        myFiles.add(new TreeItem<>("Photos", new ImageView(folderImage)));
        myFiles.add(new TreeItem<>("Vidéos", new ImageView(folderImage)));

        return myFiles;
    }

    /**
     * Create an ArrayList of TreeItems
     * @return initial directories of the "Shared Files" directory
     */
    private ArrayList<TreeItem> getSharedFiles()
    {
        ArrayList<TreeItem> myFiles = new ArrayList<>();

        myFiles.add(new TreeItem<>("Archives", new ImageView(folderImage)));
        myFiles.add(new TreeItem<>("Temp", new ImageView(folderImage)));
        return myFiles;
    }
}
