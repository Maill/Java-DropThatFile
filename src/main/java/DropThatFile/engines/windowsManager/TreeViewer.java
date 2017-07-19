package DropThatFile.engines.windowsManager;

import java.io.File;
import DropThatFile.GlobalVariables;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;
/**
 * Created by Olivier on 17/07/2017.
 */

public final class TreeViewer{
    private static String fileNameExt_matchRgx = "^[a-zA-Z0-9.-]+\\.[a-zA-Z0-9.-]+$";
    private static String folderName_matchRgx = "^[^a-zA-Z0-9-]+$";
    private static String specialChars_replaceRgx = "[^a-zA-Z0-9-]";

    /**
     * Main method which sets and configure the TreeView
     * @param treeView FXML TreeView field
     * @param icons Image array for the TreeItems icons
     * @param folderName FXML TextField field to name a new directory
     * @param synchronize FXML Button field to refresh the TreeView according to the content of the root directory
     * @param addFolder FXML Button field to add a folder with the name set in the folderName TextField
     * @param removeNode FXML Button field to remove a selected node in the TreeView
     * @param contextMenu FXML ContextMenu field to do some events like deleting a node
     * @param autoRefresh FXML CheckBox field to set an auto refresh routine
     */
    public static void setTreeView(TreeView<File> treeView, Image[] icons, TextField folderName, Button synchronize,
                                   Button addFolder, Button removeNode, ContextMenu contextMenu, CheckBox autoRefresh)
    {
        // TreeView building
        configureTreeView(treeView, icons);

        // Button events
        synchronize.setOnAction(e -> configureTreeView(treeView, icons));
        addFolder.setOnAction(e -> userAddFolderNode(treeView, icons, folderName.getText()));
        removeNode.setOnAction(e -> userRemoveNode(treeView));

        // ContextMenu setting and linking to the TreeView
        setNodeContextMenuEvent(treeView, contextMenu);

        // For the auto refresh routine
        setTimelineTreeView(treeView, icons, autoRefresh);

        // Allows edit of TreeView's nodes
        treeView.setEditable(true);
        treeView.setCellFactory(p -> new TreeTextFieldEditor());
    }

    /**
     * Used for updating the TreeView if its state change. INCOMPLETE
     * @param treeView FXML TreeView field to update
     * @param icons Image array for the TreeItem icons
     * @return Recursive parsing of the actual root directory
     */
    public static TreeItem<File> refreshNodes(TreeView<File> treeView, Image[] icons) {
        // Clears the TreeView
        removeNodes(treeView);
        // Recursive parsing of the actual root directory
        return getNodesForDirectory(new File(GlobalVariables.outputZipPath), icons);
    }

    /**
     * Set and populate the TreeView
     * @param treeView FXML TreeView field to set
     * @param icons Image array for the TreeItem icons
     */
    private static void configureTreeView(TreeView<File> treeView, Image[] icons){
        // Add a node which will imitate the actual file/folder
        treeView.setRoot(getNodesForDirectory(new File(GlobalVariables.outputZipPath), icons));
        treeView.getRoot().setExpanded(true);
        treeView.setShowRoot(true);
    }

    /**
     * Recursive parsing of the actual root directory to populate the TreeView
     * @param directory File directory
     * @param icons Image array for the TreeItem icons
     * @return Returns a File TreeItem representation of the specified directory
     */
    private static TreeItem<File> getNodesForDirectory(File directory, Image[] icons) {
        TreeItem<File> root = new TreeItem<>(new File(directory.getName()), new ImageView(icons[1]));
        try{
            for(File node : directory.listFiles()) {
                if(node.isDirectory()) { // Recursive function to get folders/files
                    root.getChildren().add(getNodesForDirectory(node, icons));
                } else
                    root.getChildren().add(new TreeItem<>(new File(node.getName()), new ImageView(icons[2])));
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        return root;
    }

    /**
     * Set a routine timer to automatically update the content of the TreeView
     * @param treeView FXML TreeView field to refresh
     * @param icons Image array for the TreeItem icons
     * @param autoRefresh FXML CheckBox field to play/pause the timer
     */
    private static void setTimelineTreeView(TreeView<File> treeView, Image[] icons, CheckBox autoRefresh){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(60000),
                        event -> configureTreeView(treeView, icons))
        );
        // No end to the timer
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.pause();

        // CheckBox selected = timer's on
        autoRefresh.setOnAction(e -> {
            if (autoRefresh.isSelected()) timeline.play();
            else timeline.pause();
        });
    }

    /**
     * Add custom events to the TreeView
     * @param treeView FXML TreeView field to add events
     * @param contextMenu ContextMenu which stores the MenuItems for the events
     */
    private static void setNodeContextMenuEvent(TreeView<File> treeView, ContextMenu contextMenu){
        // Right-click to open the ContextMenu
        EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> handleMouseClicked(event, treeView, contextMenu);
        // Delete key to remove a node
        EventHandler<KeyEvent> keyboardEventHandle = (KeyEvent event) -> handleKeyPressed(event, treeView);

        // Add custom events to the TreeView
        treeView.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEventHandle);
        treeView.addEventHandler(KeyEvent.KEY_RELEASED, keyboardEventHandle);
    }

    /**
     * Handle right-click to open the ContextMenu
     * @param event Custom event
     * @param treeView FXML TreeView field to interact with
     * @param contextMenu ContextMenu which stores the MenuItems for the events
     */
    private static void handleMouseClicked(MouseEvent event, TreeView<File> treeView, ContextMenu contextMenu) {
        // Get clicked node
        Node clickedNode = event.getPickResult().getIntersectedNode();
        // Accept clicks only on node cells, and not on empty spaces of the TreeView (maybe not 100% functional)
        if (clickedNode instanceof Text || (clickedNode instanceof TreeCell && ((TreeCell)clickedNode).getText() != null)) {
            TreeItem<File> selectedNode = treeView.getSelectionModel().getSelectedItem();

            if (event.getButton() == MouseButton.SECONDARY) {
                // Preview only for files, not folders
                if(selectedNode.getValue().isFile() && selectedNode.isLeaf()){
                    contextMenu.getItems().get(0).setDisable(false); // Enable Preview MenuItem
                } else {
                    contextMenu.getItems().get(0).setDisable(true); // Disable Preview MenuItem
                }
                treeView.setContextMenu(contextMenu);
            }
        }
    }

    /**
     * Handle delete key to remove node
     * @param event Custom event
     * @param treeView FXML TreeView field to interact with
     */
    private static void handleKeyPressed(KeyEvent event, TreeView<File> treeView){
        TreeItem<File> selectedNode = treeView.getSelectionModel().getSelectedItem();

        if (event.getCode().equals(KeyCode.DELETE) && selectedNode != null) {
            removeNode(treeView);
        }
    }

    /**
     * Method for the user to manually add a TreeItem folder to a TreeView
     * @param treeView FXML TreeView field to add a folder to.
     * @param icons Icons' array where the folder icon is located and used for the TreeView.
     * @param name Name of the folder
     * @return String log of the situation
     */
    public static String userAddFolderNode(TreeView<File> treeView, Image[] icons, String name) {
        if (name == null || name.trim().equals("")) name = "New folder"; // Just in case

        name = name.replaceAll(specialChars_replaceRgx, "_");

        TreeItem<File> selectedItem = treeView.getSelectionModel().getSelectedItem();

        if (selectedItem == null || selectedItem.getValue().isFile()) return "Select a folder to add another folder.";

        TreeItem<File> newItem = new TreeItem<>(new File(name), new ImageView(icons[1]));

        return user_isDuplicateFolder(selectedItem, newItem, name);
    }

    /**
     * Method to add a TreeItem folder to a TreeView
     * @param treeView FXML TreeView field to add a folder to.
     */
    public static void addFolderNode(TreeView<File> treeView, File folderToAdd, Image[] icons) {
        folderToAdd.getName().replaceAll(specialChars_replaceRgx, "_");

        TreeItem<File> selectedItem = treeView.getSelectionModel().getSelectedItem();

        if (selectedItem == null || selectedItem.getValue().isFile()) return;

        TreeItem<File> newItem = new TreeItem<>(folderToAdd, new ImageView(icons[1]));

        if(isDuplicateFolder(selectedItem, newItem, folderToAdd.getName())){
            return;
        }

        selectedItem.getChildren().add(newItem);

        if (!selectedItem.isExpanded()) selectedItem.setExpanded(true);
    }

    /**
     * Method to add a TreeItem file to a TreeView
     * @param treeView FXML TreeView field to add a file to.
     */
    public static void addFileNode(TreeView<File> treeView, File fileToAdd, Image[] icons) {
        // Regex checking the name for special characters not allowed on the Windows OS
        fileToAdd.getName().replaceAll(specialChars_replaceRgx, "_");

        // Get selected node in the TreeView
        TreeItem<File> selectedItem = treeView.getSelectionModel().getSelectedItem();

        // Regex checking if the name of the node matches the one of a file.
        // If so, we can't add a file under a file in the TreeView. Only in folders.
        if (selectedItem == null || selectedItem.getValue().isFile()) return;

        // Create the new node that will represent the added file
        TreeItem<File> newItem = new TreeItem<>(fileToAdd, new ImageView(icons[2]));

        if(isDuplicateFolder(selectedItem, newItem, fileToAdd.getName())){
            return;
        }

        // If selected node doesn't have any children
        if(selectedItem.isLeaf()) {
            selectedItem.getChildren().add(newItem);
            return;
        }

        // If everything is alright, then we add the node to the TreeView
        selectedItem.getChildren().add(newItem);
    }

    /**
     * Method for the user to remove a TreeItem folder from a TreeView
     * @param treeView FXML TreeView field where the TreeItem will be deleted
     * @return String log of the situation
     */
    public static String userRemoveNode(TreeView<File> treeView) {
        TreeItem<File> node = treeView.getSelectionModel().getSelectedItem();

        if (node == null) return "Select an element to delete.";

        TreeItem<File> parent = node.getParent();

        if (parent == null) return "Cannot delete a root element.";
        else parent.getChildren().remove(node);

        return "Element deleted.";
    }

    /**
     * Method to remove a TreeItem from a TreeView
     * @param treeView FXML TreeView field where the TreeItem will be deleted
     */
    public static void removeNode(TreeView<File> treeView) {
        TreeItem<File> node = treeView.getSelectionModel().getSelectedItem();

        if (node == null) return;

        TreeItem<File> parent = node.getParent();

        if (parent == null) return;
        parent.getChildren().remove(node);
    }

    /**
     * Remove all nodes from the TreeView
     * @param treeView FXML TreeView field where to delete nodes
     */
    private static void removeNodes(TreeView<File> treeView) {
        treeView.getRoot().getChildren().clear();
    }

    /**
     * Check for duplicate folders in the TreeView
     * @param selectedItem Parent TreeItem in which the new node should be inserted.
     * @param newItem The new node
     * @param name The new node's name
     * @return Boolean stating if a duplicate has been found
     */
    private static boolean isDuplicateFolder(TreeItem<File> selectedItem, TreeItem<File> newItem, String name){
        // If selected node is a folder that doesn't have any children
        if(selectedItem.isLeaf() || selectedItem.getValue().isDirectory()) {
            selectedItem.getChildren().add(newItem);
            return false;
        }

        ObservableList<TreeItem<File>> children = selectedItem.getChildren();
        int count = 0;

        // If the first child of the selected node has the same name (duplicate)
        if(children.get(count).getValue().getName().equals(newItem.getValue().getName())
                && selectedItem.getValue().isDirectory()) {
            return true;
        }

        // Parse every similar sibling (node on the same level and of the same type) to check for duplicates
        while(children.get(count).nextSibling() != null) {
            TreeItem<File> nextSibling = children.get(count).nextSibling();
            if(nextSibling.getValue().isDirectory()){
                if(nextSibling.getValue().getName().equals(name)) {
                    return true;
                }
            }
            count++;
        }
        return false;
    }

    /**
     * Check for duplicate files in the TreeView
     * @param selectedItem Parent TreeItem in which the new node should be inserted.
     * @param newItem The new node
     * @param name The new node's name
     * @return Boolean stating if a duplicate has been found
     */
    private static boolean isDuplicateFile(TreeItem<File> selectedItem, TreeItem<File> newItem, String name){
        // If selected node is a folder that doesn't have any children
        if(selectedItem.isLeaf() || selectedItem.getValue().isDirectory()) {
            selectedItem.getChildren().add(newItem);
            return false;
        }

        ObservableList<TreeItem<File>> children = selectedItem.getChildren();
        int count = 0;

        // If the first child of the selected node has the same name (duplicate)
        if(children.get(count).getValue().getName().equals(newItem.getValue().getName())
                && selectedItem.getValue().isFile()) {
            return true;
        }

        // Parse every similar sibling (node on the same level and of the same type) to check for duplicates
        while(children.get(count).nextSibling() != null) {
            TreeItem<File> nextSibling = children.get(count).nextSibling();
            if(nextSibling.getValue().isFile()){
                if(nextSibling.getValue().getName().equals(name)) {
                    return true;
                }
            }
            count++;
        }
        return false;
    }

    /**
     * Check for duplicate folders in the TreeView
     * @param selectedItem Parent TreeItem in which the new node should be inserted.
     * @param newItem The new node
     * @param name The new node's name
     * @return String log stating if a duplicate has been found
     */
    private static String user_isDuplicateFolder(TreeItem<File> selectedItem, TreeItem<File> newItem, String name){
        // If selected node doesn't have any children
        if(selectedItem.isLeaf() || selectedItem.getValue().isDirectory()) {
            selectedItem.getChildren().add(newItem);
            return "'" + name + "' folder created.";
        }

        ObservableList<TreeItem<File>> children = selectedItem.getChildren();
        int count = 0;

        // If the first child of the selected node has the same name (duplicate)
        if(children.get(count).getValue().getName().equals(name)) return "'" + name + "' folder already exists in this location.";

        // Parse every similar sibling (node on the same level and of the same type) to check for duplicates
        while(children.get(count).nextSibling() != null) {
            TreeItem<File> nextSibling = children.get(count).nextSibling();
            if(nextSibling.getValue().isDirectory()){
                if(nextSibling.getValue().getName().equals(name))
                    return "'" + name + "' folder already exists in this location.";
            }
            count++;
        }

        selectedItem.getChildren().add(newItem);

        return "'" + name + "' folder created.";
    }

    /**
     * Check for duplicate files in the TreeView
     * @param selectedItem Parent TreeItem in which the new node should be inserted.
     * @param newItem The new node
     * @param name The new node's name
     * @return String log stating if a duplicate has been found
     */
    private static String user_isDuplicateFile(TreeItem<File> selectedItem, TreeItem<File> newItem, String name){
        // If selected node doesn't have any children
        if(selectedItem.isLeaf() || selectedItem.getValue().isFile()) {
            selectedItem.getChildren().add(newItem);
            return "'" + name + "' file created.";
        }

        ObservableList<TreeItem<File>> children = selectedItem.getChildren();
        int count = 0;

        // If the first child of the selected node has the same name (duplicate)
        if(children.get(count).getValue().getName().equals(name)) return "'" + name + "' file already exists in this location.";

        // Parse every similar sibling (node on the same level and of the same type) to check for duplicates
        while(children.get(count).nextSibling() != null) {
            TreeItem<File> nextSibling = children.get(count).nextSibling();
            if(nextSibling.getValue().isFile()){
                if(nextSibling.getValue().getName().equals(name))
                    return "'" + name + "' file already exists in this location.";
            }
            count++;
        }

        selectedItem.getChildren().add(newItem);

        return "'" + name + "' file created.";
    }
}