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
import javafx.scene.layout.AnchorPane;

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
        setTreeView_repository(treeView_repository, "root");
    }

    //region TreeView
    /**
     * Configuration of the TreeView
     * @param treeView fx:id of a TreeView element
     * @param rootNode root node name
     */
    private void setTreeView_repository(TreeView treeView, String rootNode){
        TreeViewRepository repo = new TreeViewRepository();
        ArrayList<TreeItem> all = repo.getAll();
        TreeItem rootItem = new TreeItem(rootNode);
        // Expand the treeView
        rootItem.setExpanded(true);
        // Add children to the root
        rootItem.getChildren().addAll(all);
        // Set the Root File
        treeView.setRoot(rootItem);
        // Set a cell factory to use TextFieldTreeCell
        treeView.setCellFactory(TextFieldTreeCell.forTreeView());
        // Trigger treeView events
        treeView_events(treeView);
    }

    /**
     * Events of the treeView
     */
    private void treeView_events(TreeView treeView){
        // Set editing related event handlers (OnEditCommit)
        treeView.setOnEditCommit(event -> editCommit((TreeView.EditEvent) event));
        // Create the item with the typed name
        item_add.setOnAction(event -> addItem(treeView, item_textField.getText()));
        // Remove the selected item
        item_remove.setOnAction(event -> removeItem(treeView));
    }

    /**
     * What happens on commit
     * @param event editOnCommit
     */
    private void editCommit(TreeView.EditEvent event)
    {
        //TODO : EVENT - communication avec la BDD lorsque le nom est modifié
        return;
    }

    /**
     * Method for Adding an TreeItem
     * @param value TreeItem name
     * @param treeView fx:id of a TreeView element
     */
    private void addItem(TreeView treeView, String value)
    {
        if (value == null || value.trim().equals(""))
        {
            this.writeMessage("Directory name cannot be empty.");
            return;
        }

        TreeItem parent = (TreeItem) treeView.getSelectionModel().getSelectedItem();

        if (parent == null)
        {
            this.writeMessage("Select a directory to add this directory to.");
            return;
        }

        if (((TreeItem) treeView.getSelectionModel().getSelectedItem()).getValue().equals("root")){
            this.writeMessage("Cannot create a directory from the root directory.");
            return;
        }

        TreeItem newItem = new TreeItem(value);
        parent.getChildren().add(newItem);

        if (!parent.isExpanded())
        {
            parent.setExpanded(true);
        }
    }

    /**
     * Method for Removing an TreeItem
     * @param treeView fx:id of a TreeView element
     */
    private void removeItem(TreeView treeView)
    {
        TreeItem item = (TreeItem) treeView.getSelectionModel().getSelectedItem();

        if (item == null)
        {
            this.writeMessage("Select a directory to remove.");
            return;
        }

        TreeItem parent = item.getParent();
        if (parent == null || item.getValue().equals("My Files") || item.getValue().equals("Shared Files"))
        {
            this.writeMessage("Cannot remove main directories.");
        }
        else
        {
            parent.getChildren().remove(item);
        }
    }

    /**
     * User's logs in the textArea
      */
    private void writeMessage(String msg)
    {
        this.message_textArea.appendText(msg + "\n");
    }
    //endregion
}
