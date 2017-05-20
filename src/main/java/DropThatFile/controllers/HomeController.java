package DropThatFile.controllers;

import DropThatFile.engines.windowsManager.TreeViewRepository;
import DropThatFile.engines.windowsManager.forms.HomeForm;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Travail on 02/05/2017.
 */
public class HomeController extends AnchorPane implements Initializable {
    //region FXML
    @FXML
    public void openLink() throws URISyntaxException, IOException {
        Desktop desktop = Desktop.getDesktop();
        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(new URI("www.google.com"));
        } else {
            return;
        }
    }

    @FXML
    private TreeView treeView_repository;

    @FXML
    private StackPane stackPane_repository;

    @FXML
    private TextArea message_textArea;

    @FXML
    private TextArea item_textArea;

    @FXML
    private TextField item_textField;

    @FXML
    private Button item_add;

    @FXML
    private Button item_remove;
    //endregion

    private HomeForm application;

    public void setApp(HomeForm application){
        this.application = application;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTreeView_repository();
    }

    //region TreeView
    private void setTreeView_repository(){
        TreeViewRepository repo = new TreeViewRepository();
        ArrayList<TreeItem> all = repo.getAll();
        TreeItem rootItem = new TreeItem("Root");
        //rootItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("rootFolder.png"))));
        rootItem.setExpanded(true);
        // Add children to the root
        rootItem.getChildren().addAll(all);
        // Set the Root File
        treeView_repository.setRoot(rootItem);
        // Allow editing from user
        treeView_repository.setEditable(true);

        //region Events
        // Set a cell factory to use TextFieldTreeCell
        treeView_repository.setCellFactory(TextFieldTreeCell.forTreeView());
        // Set editing related event handlers (OnEditCommit)
        treeView_repository.setOnEditCommit(event -> editCommit((TreeView.EditEvent) event));
        // Create the item with the typed name
        item_add.setOnAction(event -> addItem(item_textField.getText()));
        // Remove the selected item
        item_remove.setOnAction(event -> removeItem());
        //endregion
    }

    //Event editOnCommit
    private void editCommit(TreeView.EditEvent event)
    {
        //TODO : EVENT - communication avec la BDD lorsque le nom est modifié
        return;
    }

    // Method for Adding an Item
    private void addItem(String value)
    {
        if (value == null || value.trim().equals(""))
        {
            this.writeMessage("directory name cannot be empty.");
            return;
        }

        TreeItem parent = (TreeItem)treeView_repository.getSelectionModel().getSelectedItem();

        if (parent == null)
        {
            this.writeMessage("Select a directory to add this directory to.");
            return;
        }

        TreeItem newItem = new TreeItem(value);
        parent.getChildren().add(newItem);

        if (!parent.isExpanded())
        {
            parent.setExpanded(true);
        }
    }

    // Method for Removing an Item
    private void removeItem()
    {
        TreeItem item = (TreeItem)treeView_repository.getSelectionModel().getSelectedItem();

        if (item == null)
        {
            this.writeMessage("Select a directory to remove.");
            return;
        }

        TreeItem parent = item.getParent();
        if (parent == null )
        {
            this.writeMessage("Cannot remove the root directory.");
        }
        else
        {
            parent.getChildren().remove(item);
        }
    }

    // Logs of user in the textArea
    private void writeMessage(String msg)
    {
        this.message_textArea.appendText(msg + "\n");
    }
    //endregion
}
