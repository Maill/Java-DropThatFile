package DropThatFile.engines.windowsManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

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

import static DropThatFile.GlobalVariables.*;

/**
 * Created by Olivier on 17/06/2017.
 */

public final class TreeViewer{
    private static String specialCharsFolder_replaceRgx = "[^a-zA-Z0-9-\\p{L}\\p{M}*]";
    private static String specialCharsFile_replaceRgx = "[^a-zA-Z0-9-.\\p{L}\\p{M}*]";

    //region TreeView and nodes construction
    /**
     * Main method which sets and configure the TreeView
     * @param treeView FXML TreeView field
     * @param icons Image array for the TreeItems icons
     * @param contextMenu FXML ContextMenu field to do some events like deleting a node
     * @param autoRefresh FXML CheckBox field to set an auto refresh routine
     */
    public static void buildTreeView(TreeView<File> treeView, Image[] icons, ContextMenu contextMenu,
                                     CheckBox autoRefresh, String repoPath)
    {
        // TreeView building
        setNodes(treeView, icons, repoPath);

        // ContextMenu setting and linking to the TreeView
        setNodeContextMenuEvent(treeView, contextMenu);

        // For the auto refresh routine
        setTimelineTreeView(treeView, icons, autoRefresh, repoPath);

        // Allows edit of TreeView's nodes
        treeView.setEditable(false);
        treeView.setCellFactory(p -> new TreeTextFieldEditor());
    }

    /**
     * Set and populate the TreeView
     * @param treeView FXML TreeView field to set
     * @param icons Image array for the TreeItem icons
     */
    public static void setNodes(TreeView<File> treeView, Image[] icons, String repoPath){
        ArrayList<TreeItem<File>> expandedNodes;
        try {
            File f = new File(repoPath);
            if (!f.exists() || !f.isDirectory()) {
                f.mkdirs();
                return;
            }

            // Get former expanded nodes before refreshing the TreeView
            expandedNodes = getExpandedNodes(treeView);

            // Add a node which will imitate the actual file/folder
            treeView.setRoot(getNodesForDirectory(new File(repoPath), icons));
            treeView.getRoot().setExpanded(true);
            treeView.setShowRoot(true);

            if(expandedNodes == null || expandedNodes.size() == 0) return;
            setExpandedNodes(treeView, expandedNodes);
        } catch(NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Recursive parsing of the actual root directory to populate the TreeView
     * @param rootDirectory File directory
     * @param icons Image array for the TreeItem icons
     * @return Returns a File TreeItem representation of the specified directory
     */
    private static TreeItem<File> getNodesForDirectory(File rootDirectory, Image[] icons) {
        TreeItem<File> root = new TreeItem<>(rootDirectory, new ImageView(icons[0]));
        String nodeName;
        try{
            for(File node : rootDirectory.listFiles()) {
                if(node.isDirectory()) // Recursive function to get folders/files
                    root.getChildren().add(getNodesForDirectory(node, icons));
                else {
                    nodeName = node.getName();

                    if(nodeName.endsWith(".zip"))
                        root.getChildren().add(new TreeItem<>(node, new ImageView(icons[2])));
                    else if(nodeName.endsWith(".txt"))
                        root.getChildren().add(new TreeItem<>(node, new ImageView(icons[3])));
                    else if(nodeName.endsWith(".png") || nodeName.endsWith(".jpg") || nodeName.endsWith(".jpeg")
                            || nodeName.endsWith(".bmp")) {
                        root.getChildren().add(new TreeItem<>(node, new ImageView(icons[4])));
                    }
                    else if(nodeName.endsWith(".doc") || nodeName.endsWith(".docx"))
                        root.getChildren().add(new TreeItem<>(node, new ImageView(icons[5])));
                    else
                        root.getChildren().add(new TreeItem<>(node, new ImageView(icons[1])));

                }
            }
            return root;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return root;
        }
    }
    //endregion

    //region Expanded nodes
    /**
     * Get current expanded nodes of the TreeView
     * @param treeView FXML TreeView field where to get the expanded nodes
     * @return An ArrayList of expanded nodes
     */
    private static ArrayList<TreeItem<File>> getExpandedNodes(TreeView<File> treeView){
        ArrayList<TreeItem<File>> expandedNodes = new ArrayList<>();
        if(treeView == null || treeView.getRoot() == null || treeView.getRoot().isLeaf()) return expandedNodes;

        for(TreeItem<File> node : treeView.getRoot().getChildren()) {
            if(node.isExpanded() && node.getValue().isDirectory())
                expandedNodes.add(node);
        }
        return expandedNodes;
    }

    /**
     * Set expanded nodes in the TreeView based on the former expanded nodes
     * @param treeView FXML TreeView field where to set the expanded nodes
     * @param prevExpNodes An ArrayList of the former expanded nodes
     * @return An ArrayList of expanded nodes
     */
    private static void setExpandedNodes(TreeView<File> treeView, ArrayList<TreeItem<File>> prevExpNodes){
        if(prevExpNodes == null || prevExpNodes.size() == 0) return;

        ObservableList<TreeItem<File>> currentTreeItems = treeView.getRoot().getChildren();
        boolean isFolder = false, isNameEqual = false;
        String currentNodeName;

        for (TreeItem<File> currentTreeItem : currentTreeItems) {
            isFolder = currentTreeItem.getValue().isDirectory();
            currentNodeName = currentTreeItem.getValue().getName();

            for (TreeItem<File> prevExpNode : prevExpNodes) {
                isNameEqual = currentNodeName.equals(prevExpNode.getValue().getName());

                if(isFolder && isNameEqual)
                    currentTreeItem.setExpanded(true);
            }
        }
    }
    //endregion

    //region Events
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
                // Preview files only
                if(selectedNode.getValue().isFile() && selectedNode.isLeaf()){
                    contextMenu.getItems().get(0).setDisable(false); // Enable Preview MenuItem
                    contextMenu.getItems().get(1).setDisable(false); // Enable Open MenuItem
                } else {
                    contextMenu.getItems().get(0).setDisable(true); // Disable Preview MenuItem
                    contextMenu.getItems().get(1).setDisable(true); // Disable Open MenuItem
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
            treeView.setEditable(false);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirming dialog");
            alert.setHeaderText("Delete this?");
            alert.setContentText("Are you sure you want to delete this?");

            Optional<ButtonType> result = alert.showAndWait();
            treeView.setEditable(true);
            if (result.isPresent() && result.get() == ButtonType.OK){
                userRemoveNode(treeView);
            }
        }
        // If double-click primary button if detected on a node file
        if(event.getCode().equals(KeyCode.ENTER) && selectedNode != null) {
            try {
                File file = selectedNode.getValue();
                // We open the actual file with the default program installed on the OS
                if(file.isFile() && selectedNode.isLeaf()){
                    if(file.canExecute()){
                        Desktop.getDesktop().open(file);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    //endregion

    //region Remove Node
    /**
     * Method for the user to remove a TreeItem node, from a TreeView, and its associated file
     * @param treeView FXML TreeView field where the TreeItem is located
     * @return String log of the situation
     */
    public static String userRemoveNode(TreeView<File> treeView) {
        TreeItem<File> node = treeView.getSelectionModel().getSelectedItem();

        if (node == null) return "Select an element to delete.";

        TreeItem<File> parent = node.getParent();

        if (parent == null) return "Cannot delete a root element.";
        else {
            try {
                if(node.getValue().isDirectory()){
                    Path rootPath = Paths.get(node.getValue().getAbsolutePath());
                    Files.walk(rootPath) // Recursively get all child files
                            .sorted(Comparator.reverseOrder()) // Reverse the order of the list
                            .map(Path::toFile) // Transform each Path object to a File object
                            .forEach(File::delete); // Delete each File object
                } else
                    node.getValue().delete();

                parent.getChildren().remove(node);
                return "Element deleted.";
            } catch(Exception ex) {
                ex.printStackTrace();
                return "Element could not be deleted due to an unknown error.";
            }
        }
    }

    /**
     * Method to remove a TreeItem node, from a TreeView, and its associated file
     * @param treeView FXML TreeView field where the TreeItem is located
     */
    public static boolean removeNode(TreeView<File> treeView) {
        TreeItem<File> node = treeView.getSelectionModel().getSelectedItem();

        if (node == null) return false;

        TreeItem<File> parent = node.getParent();

        if (parent == null) return false;
        else {
            try {
                if(node.getValue().isDirectory()){
                    Path rootPath = Paths.get(node.getValue().getAbsolutePath());
                    Files.walk(rootPath) // Recursively get all child files
                            .sorted(Comparator.reverseOrder()) // Reverse the order of the list
                            .map(Path::toFile) // Transform each Path object to a File object
                            .forEach(File::delete); // Delete each File object
                } else
                    node.getValue().delete();

                parent.getChildren().remove(node);
                return true;
            } catch(Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Remove all nodes from the TreeView
     * @param treeView FXML TreeView field where to delete nodes
     */
    private static void removeNodes(TreeView<File> treeView) {
        treeView.getRoot().getChildren().clear();
    }
    //endregion

    //region Add Node
    /**
     * Method for the user to manually add a TreeItem folder to a TreeView
     * @param treeView FXML TreeView field to add a folder to.
     * @param icons Icons' array where the folder icon is located and used for the TreeView.
     * @param newFolder_name Name of the folder
     * @return String log of the situation
     */
    public static String userAddFolderNode(TreeView<File> treeView, Image[] icons, String newFolder_name) {
        if (newFolder_name == null || newFolder_name.trim().equals("")){
            newFolder_name = "New_folder"; // Just in case
        }

        // Regex checking the name for special characters not allowed on the Windows OS
        newFolder_name = newFolder_name.replaceAll(specialCharsFolder_replaceRgx, "_");

        // Get selected node in the TreeView
        TreeItem<File> selectedItem = treeView.getSelectionModel().getSelectedItem();

        // Check if the selected node is a folder or a file
        if (selectedItem == null || selectedItem.getValue().isFile()) return "You can only add a folder in a folder.";

        // Create a File instance
        TreeItem<File> newItem = new TreeItem<>(selectedItem.getValue());

        if(!user_isDuplicateFolder(selectedItem, newItem)){
            try {
                Path newFolderPath = Paths.get(newItem.getValue().getAbsolutePath() + "\\" + newFolder_name);
                if(!Files.isDirectory(Paths.get(newFolderPath.toAbsolutePath().toString()))){
                    Files.createDirectory(newFolderPath);
                    selectedItem.getChildren().add(new TreeItem<>(newFolderPath.toFile(), new ImageView(icons[0])));
                } else {
                    return "'" + newFolder_name + "' folder already exists in this location.";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "'" + newFolder_name + "' folder couldn't be created due to an error.";
            }

            return "'" + newFolder_name + "' folder created.";

        }
        return "'" + newFolder_name + "' folder already exists in this location.";
    }

    /**
     * Method for the user to manually add a TreeItem file to a TreeView
     * @param treeView FXML TreeView field to add a file to.
     * @param icons Icons' array where the file icon is located and used for the TreeView.
     * @param fileToAdd File to add upload
     * @return String log of the situation
     */
    public static String userAddFileNode(TreeView<File> treeView, Image[] icons, File fileToAdd) {
        // Get selected node in the TreeView
        TreeItem<File> selectedItem = treeView.getSelectionModel().getSelectedItem();

        // Check if the selected node is a folder or a file
        if (selectedItem == null || selectedItem.getValue().isFile()) return "You can only add a file in a folder.";

        // Create the new file
        TreeItem<File> newItem = new TreeItem<>(fileToAdd, new ImageView(icons[1]));

        if(!user_isDuplicateFile(selectedItem, newItem)){
            try {
                Files.copy(
                        Paths.get(newItem.getValue().getAbsolutePath()),
                        Paths.get(selectedItem.getValue().getAbsolutePath() + "\\" + newItem.getValue().getName())
                );
                selectedItem.getChildren().add(newItem);
            } catch (IOException ex) {
                ex.printStackTrace();
                return "'" + fileToAdd.getName() + "' file couldn't be created due to an error.";
            }

            return "'" + fileToAdd.getName() + "' file created.";

        }
        return "'" + fileToAdd.getName() + "' file already exists in this location.";
    }

    /**
     * Method to add a TreeItem folder to a TreeView
     * @param treeView FXML TreeView field to add a folder to.
     * @param folderToAdd folder to add
     * @param icons Icons' array where the folder icon is located and used for the TreeView.
     */
    @Deprecated
    public static void addFolderNode(TreeView<File> treeView, File folderToAdd, Image[] icons) {
        // Regex checking the name for special characters not allowed on the Windows OS
        folderToAdd.getName().replaceAll(specialCharsFolder_replaceRgx, "_");

        // Get selected node in the TreeView
        TreeItem<File> selectedItem = treeView.getSelectionModel().getSelectedItem();

        // Check if the selected node is a folder or a file
        if (selectedItem == null || selectedItem.getValue().isFile()) return;

        // Create the new folder
        TreeItem<File> newItem = new TreeItem<>(folderToAdd, new ImageView(icons[0]));

        if(!isDuplicateFolder(selectedItem, newItem)){
            // If everything is ok, then we add the node to the TreeView
            selectedItem.getChildren().add(newItem);
            if (!selectedItem.isExpanded()) selectedItem.setExpanded(true);
        }
    }

    /**
     * Method to add a TreeItem file to a TreeView
     * @param treeView FXML TreeView field to add a file to.
     * @param fileToAdd File to add
     * @param icons Icons' array where the file icon is located and used for the TreeView.
     */
    @Deprecated
    public static void addFileNode(TreeView<File> treeView, File fileToAdd, Image[] icons) {
        // Regex checking the name for special characters not allowed on the Windows OS
        fileToAdd.getName().replaceAll(specialCharsFile_replaceRgx, "_");

        // Get selected node in the TreeView
        TreeItem<File> selectedItem = treeView.getSelectionModel().getSelectedItem();

        // Check if the selected node is a folder or a file
        if (selectedItem == null || selectedItem.getValue().isFile()) return;

        // Create the new file
        TreeItem<File> newItem = new TreeItem<>(fileToAdd, new ImageView(icons[1]));

        if(!isDuplicateFile(selectedItem, newItem)){
            // If everything is ok, then we add the node to the TreeView
            selectedItem.getChildren().add(newItem);
        }
    }

    /**
     * Check for duplicate folders in the TreeView
     * @param selectedItem Parent TreeItem in which the new node should be inserted.
     * @param newItem The new node
     * @return Boolean stating if a duplicate has been found
     */
    private static boolean isDuplicateFolder(TreeItem<File> selectedItem, TreeItem<File> newItem){
        String newNodeName = newItem.getValue().getName();

        // If selected node is a folder that doesn't have any children
        if(selectedItem.isLeaf() && selectedItem.getValue().isDirectory()) return false;

        ObservableList<TreeItem<File>> children = selectedItem.getChildren();
        int count = 0;

        if(children.isEmpty()) return false;
        // If the first child of the selected node has the same name (duplicate)
        if(children.get(count).getValue().getName().equals(newNodeName)
                && selectedItem.getValue().isDirectory()) return true;

        // Parse every similar sibling (node on the same level and of the same type) to check for duplicates
        while(children.get(count).nextSibling() != null) {
            TreeItem<File> nextSibling = children.get(count).nextSibling();
            if(nextSibling.getValue().isDirectory()){
                if(nextSibling.getValue().getName().equals(newNodeName)) {
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
     * @return Boolean stating if a duplicate has been found
     */
    private static boolean isDuplicateFile(TreeItem<File> selectedItem, TreeItem<File> newItem){
        String newNodeName = newItem.getValue().getName();

        // If selected node is a folder that doesn't have any children
        if(selectedItem.isLeaf() && selectedItem.getValue().isDirectory()) return false;

        ObservableList<TreeItem<File>> children = selectedItem.getChildren();
        int count = 0;

        if(children.isEmpty()) return false;
        // If the first child of the selected node has the same name (duplicate)
        if(children.get(count).getValue().getName().equals(newNodeName)
                && selectedItem.getValue().isFile()) return true;

        // Parse every similar sibling (node on the same level and of the same type) to check for duplicates
        while(children.get(count).nextSibling() != null) {
            TreeItem<File> nextSibling = children.get(count).nextSibling();
            if(nextSibling.getValue().isFile()){
                if(nextSibling.getValue().getName().equals(newNodeName)) {
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
     * @return String log stating if a duplicate has been found
     */
    private static boolean user_isDuplicateFolder(TreeItem<File> selectedItem, TreeItem<File> newItem){
        String newNodeName = newItem.getValue().getName();
        System.out.println(newItem.getValue().getName() + " | " + newItem.getValue().isDirectory());

        // If selected node doesn't have any children
        if(selectedItem.isLeaf() && selectedItem.getValue().isDirectory()) return false;

        ObservableList<TreeItem<File>> children = selectedItem.getChildren();
        int count = 0;

        if(children.isEmpty()) return false;
        // If the first child of the selected node has the same name (duplicate)
        if(children.get(count).getValue().getName().equals(newNodeName)
                && selectedItem.getValue().isDirectory()) return true;

        // Parse similar siblings (nodes on the same level and of the same type) to check for duplicates
        while(children.get(count).nextSibling() != null) {
            TreeItem<File> nextSibling = children.get(count).nextSibling();
            if(nextSibling.getValue().getName().equals(newNodeName) && nextSibling.getValue().isDirectory())
                return true;
            count++;
        }
        return false;
    }

    /**
     * Check for duplicate files in the TreeView
     * @param selectedItem Parent TreeItem in which the new node should be inserted.
     * @param newItem The new node
     * @return String log stating if a duplicate has been found
     */
    private static boolean user_isDuplicateFile(TreeItem<File> selectedItem, TreeItem<File> newItem){
        String newNodeName = newItem.getValue().getName();

        // If selected node doesn't have any children
        if(selectedItem.isLeaf() && selectedItem.getValue().isFile()) return false;

        ObservableList<TreeItem<File>> children = selectedItem.getChildren();
        int count = 0;

        if(children.isEmpty()) return false;
        // If the first child of the selected node has the same name (duplicate)
        if(children.get(count).getValue().getName().equals(newNodeName)
                && selectedItem.getValue().isFile()) return true;

        // Parse every similar sibling (node on the same level and of the same type) to check for duplicates
        while(children.get(count).nextSibling() != null) {
            TreeItem<File> nextSibling = children.get(count).nextSibling();
            if(nextSibling.getValue().isFile()){
                if(nextSibling.getValue().getName().equals(newNodeName))
                    return true;
            }
            count++;
        }
        return false;
    }

    //endregion

    //region Misc
    /**
     * Set a routine timer to automatically update the content of the TreeView
     * @param treeView FXML TreeView field to refresh
     * @param icons Image array for the TreeItem icons
     * @param autoRefresh FXML CheckBox field to play/pause the timer
     */
    private static void setTimelineTreeView(TreeView<File> treeView, Image[] icons, CheckBox autoRefresh, String repoPath){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(60000),
                        event -> setNodes(treeView, icons, repoPath))
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
     * Open a selected file via primary mouse button click or enter key
     * @param treeView FXML TreeView field where the selected node is located
     * @return String log of the situation
     */
    public static String openFile(TreeView<File> treeView){
        TreeItem<File> selectedNode = treeView.getSelectionModel().getSelectedItem();
        File selectedFile = selectedNode.getValue();

        if(!selectedFile.exists())
            return "'" + selectedFile.getName() + "' file doesn't exist.";

        try {
            // We open the actual file with the default program installed on the OS
            if(selectedFile.isFile() && selectedNode.isLeaf()){
                if(selectedFile.canExecute() || selectedFile.canRead()){
                    Desktop.getDesktop().open(selectedFile);
                }
                else if(!selectedFile.canExecute() && !selectedFile.canRead())
                    return "'" + selectedFile.getName() + "' file cannot be opened at the moment.";
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return "'" + selectedFile.getName() + "' file couldn't be opened due to an error.";
        }
        return "'" + selectedFile.getName() + "' file successfully opened.";
    }
    //endregion
}