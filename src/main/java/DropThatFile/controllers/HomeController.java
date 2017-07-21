package DropThatFile.controllers;

import DropThatFile.engines.APIData.APIModels.APIGroup;
import DropThatFile.engines.FilesJobs;
import DropThatFile.engines.LogManagement;
import DropThatFile.engines.windowsManager.WindowsHandler;
import DropThatFile.models.Group;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.lingala.zip4j.exception.ZipException;
import org.apache.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import static DropThatFile.engines.windowsManager.TreeViewer.*;
import static DropThatFile.GlobalVariables.*;

/**
 * Created by Travail on 02/05/2017.
 */
public class HomeController extends AnchorPane implements Initializable {
    //region FXML
    @FXML
    private TabPane repositories_tabPane;

    @FXML
    private Tab myRepository_tab;

    @FXML
    private Button button_archiveBrowse;

    @FXML
    private Button button_browse;

    @FXML
    private Button button_synchronize;

    @FXML
    public TreeView<File> treeView_repository;

    @FXML
    private CheckBox checkBox_autoRefresh;

    @FXML
    private TextArea message_textArea;

    @FXML
    private TextArea preview_textArea;

    @FXML
    private TextField folderName_textField;

    @FXML
    private Button createFolder_button;

    @FXML
    private Button removeFolder_button;

    @FXML
    public void openLink() throws URISyntaxException, IOException {
        if(!desktop.isSupported(Desktop.Action.BROWSE)) return;
        desktop.browse(new URI("http://localhost:8080/DropThatFile-Web/"));
    }
    //endregion

    // Dropdown menu on right-click in the TreeView
    private ContextMenu contextMenu = new ContextMenu();
    private MenuItem preview_menuItem = new MenuItem("Preview");
    private MenuItem open_menuItem = new MenuItem("Open File");
    private MenuItem addFolder_menuItem = new MenuItem("Add folder");
    private MenuItem delete_menuItem = new MenuItem("Delete");

    // Icons used for the nodes in the TreeView
    private Image[] icons = new Image[]{
            new Image(getClass().getResourceAsStream("/images/opFolder.png")),
            new Image(getClass().getResourceAsStream("/images/file.png"))
    };

    // Log4j instance
    private final Logger log = LogManagement.getInstanceLogger(this);

    // Launch the default web browser
    private Desktop desktop = Desktop.getDesktop();

    private Stage stage = new Stage();

    private WindowsHandler windowsHandler = new WindowsHandler(stage);

    //private ArrayList<Stage> pluginStages = new ArrayList<>();
    //private HashMap<String, String> jarPaths = new HashMap<>();

    public static String zipPassword = null;
    public static String zipName = null;
    public static String zipDescription = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buildTreeView(treeView_repository, icons, contextMenu, checkBox_autoRefresh, currentUserRepoPath);
        setAllControls();
        setUserGroupsTabPanes();
    }

    /**
     * Create a Tab in the PaneTab for each group where the user is a member of.
     */
    private void setUserGroupsTabPanes(){
        try {
            HashMap<Integer, Group> groupsOfCurrentUser = APIGroup.Instance().getGroupsForUser();
            if(groupsOfCurrentUser.isEmpty()) return;

            int i = 0;
            for (Group group : groupsOfCurrentUser.values()) {
                if(!currentGroups.contains(group)) currentGroups.add(group);
                String groupName = currentGroups.get(i).getName();
                String currentGroupPath = groupRepoMainPath.concat(groupName) + "\\";

                Tab groupTab = new Tab(groupName);
                groupTab.setClosable(false);
                groupTab.setOnSelectionChanged(e1 -> {
                    setNodes(treeView_repository, icons, currentGroupPath);
                    button_synchronize.setOnAction(null);
                    button_synchronize.setOnAction(e2 -> setNodes(treeView_repository, icons, currentGroupPath));
                });

                repositories_tabPane.getTabs().add(groupTab);

                i++;
            }
        } catch(IndexOutOfBoundsException ex){
            ex.printStackTrace();
        } finally {
            myRepository_tab.setOnSelectionChanged(e -> {
                setNodes(treeView_repository, icons, currentUserRepoPath);
                button_synchronize.setOnAction(null);
                button_synchronize.setOnAction(e2 -> setNodes(treeView_repository, icons, currentUserRepoPath));
            });
            button_synchronize.setOnAction(null);
            button_synchronize.setOnAction(e -> setNodes(treeView_repository, icons, currentUserRepoPath));
        }
    }

    /**
     * Set all controls and their events
     */
    private void setAllControls(){
        setArchiveBrowseButton();
        setBrowseButton();

        contextMenu.getItems().addAll(preview_menuItem, open_menuItem, addFolder_menuItem, delete_menuItem);
        // Preview MenuItem
        contextMenu.getItems().get(0).setOnAction(e -> {});
        // Open MenuItem
        contextMenu.getItems().get(1).setOnAction(e -> this.writeMessage(openFile(treeView_repository)));
        // Add folder MenuItem
        contextMenu.getItems().get(2).setOnAction(e ->
                this.writeMessage(userAddFolderNode(treeView_repository, icons, "New_folder")));
        // Delete MenuItem
        contextMenu.getItems().get(3).setOnAction(e -> this.writeMessage(userRemoveNode(treeView_repository)));

        // Button events
        button_synchronize.setOnAction(null);
        button_synchronize.setOnAction(e -> setNodes(treeView_repository, icons, currentUserRepoPath));
        createFolder_button.setOnAction(e -> userAddFolderNode(treeView_repository, icons, folderName_textField.getText()));
        removeFolder_button.setOnAction(e -> userRemoveNode(treeView_repository));
    }

    /**
     * Create a modal that allows the user to choose an archive name and a password for it
     */
    private void modalSetArchiveFile(){
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(windowsHandler.getJfxStage());

        Label zipNameLabel = new Label("Archive name*:");
        TextField zipNameTextField = new TextField();
        zipNameTextField.setPromptText("Name...");
        Label zipPasswordLabel = new Label("Archive password*:");
        PasswordField zipPasswordTextField = new PasswordField();
        zipPasswordTextField.setPromptText("Password...");
        Label zipDescriptionLabel = new Label("Archive description:");
        TextField zipDescriptionTextField = new TextField();
        zipDescriptionTextField.setPromptText("Description...");

        Button btnValidate = new Button("Continue");

        btnValidate.setOnMouseClicked((e) -> {
            zipName = zipNameTextField.getText();
            zipPassword = zipPasswordTextField.getText();
            zipDescription = zipDescriptionTextField.getText();

            if (zipName != null && zipPassword != null) {
                if(zipName.length() >= 1 && zipPassword.length() >= 1){
                    if(zipDescriptionTextField.getText().trim().isEmpty())
                        zipDescription = "No description available";
                    dialog.close();
                } else {
                    if(zipNameTextField.getText() == null)
                        zipNameTextField.setPromptText("Please set a name to the archive.");
                    if(zipPasswordTextField.getText() == null)
                        zipPasswordTextField.setPromptText("Please set a password.");
                }
            }
        });

        HBox dialogHbox = new HBox(10);
        dialogHbox.getChildren().add(new Text("To set an archive: enter a fileName, a password, plus an optional description."));
        dialogHbox.getChildren().add(btnValidate);
        VBox dialogVox = new VBox(10);
        dialogVox.getChildren().addAll(
                zipNameLabel, zipNameTextField,
                zipPasswordLabel, zipPasswordTextField,
                zipDescriptionLabel, zipDescriptionTextField
        );

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30)); // space between elements and window border
        root.setTop(dialogHbox);
        root.setCenter(dialogVox);
        root.setBottom(btnValidate);

        Scene dialogScene = new Scene(root, 500, 275);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    /**
     * Initialize the archive browse button
     */
    private void setArchiveBrowseButton() {
        // File browser
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose some files");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files", "*.*"));
        Stage fileChooserStage = new Stage();
        button_archiveBrowse.setOnAction(e -> {
            modalSetArchiveFile();
            // Retrieve current selected item
            TreeItem selectedTreeItem = treeView_repository.getSelectionModel().getSelectedItem();

            if (treeView_repository.getSelectionModel().isEmpty() || selectedTreeItem.getValue() == null) {
                this.writeMessage("Please select a proper directory.");
            }
            else if (zipName == null || zipPassword == null){
                this.writeMessage("Please set proper archive name and password .");
            } else {
                // Allows multiple selection of files in the file browser
                List<File> list = fileChooser.showOpenMultipleDialog(fileChooserStage);
                if (list != null) {
                    try {
                        processArchive(list);
                    } catch(IOException ex){
                        this.writeMessage("Error while trying to reach browsed file."  + ex.getMessage());
                        log.error("Erreur en tentant d'accéder au fichier parcouru.\nMessage : \n" + ex.getMessage());
                    }
                }
            }
        });
    }

    /**
     * Initialize the normal file browse button
     */
    private void setBrowseButton() {
        // File browser
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files", "*.*"));
        Stage fileChooserStage = new Stage();

        button_browse.setOnAction(e -> {
            // Retrieve current selected item
            TreeItem selectedTreeItem = treeView_repository.getSelectionModel().getSelectedItem();

            if (treeView_repository.getSelectionModel().isEmpty() || selectedTreeItem.getValue() == null) {
                this.writeMessage("Please select a proper directory.");
            } else {
                // Allows multiple selection of files in the file browser
                File fileToAdd = fileChooser.showOpenDialog(fileChooserStage);
                if (fileToAdd != null) {
                    try {
                        processFile(fileToAdd);
                    } catch(IOException ex){
                        this.writeMessage("Error while trying to reach the file."  + ex.getMessage());
                        log.error("Erreur en tentant d'accéder au fichier parcouru.\nMessage : \n" + ex.getMessage());
                    }
                }
            }
        });
    }

    /**
     * Check for unreadable files before sending them to the server
     * @param files List of files to send
     * @throws IOException
     * @throws ZipException
     */
    private void processArchive(List<File> files) throws IOException {
        // Check if the files exist and if they can be read
        for (File currentFile : files) {
            if (!currentFile.canRead()) {
                this.writeMessage("Error while trying to reach one of the browsed files.");
                return;
            }
        }

        DropThatFile.models.File archiveToUpload = new DropThatFile.models.File(1, zipName, zipPassword, Date.from(Instant.now()), zipDescription);

        // Add selected files into an encrypted zip file
        try {
            FilesJobs.Instance().sendEncryptedArchive(archiveToUpload, files);
            //TODO: Send the File model Object to the database
            this.writeMessage("Archive successfully uploaded.");
        } catch (ZipException ex) {
            ex.printStackTrace();
            log.error("Erreur en tentant de créer une archive encryptée.\nMessage : \n" + ex.getMessage());
        }
    }

    /**
     * Check for unreadable files before sending them to the server
     * @param fileToAdd File to send
     * @throws IOException
     * @throws ZipException
     */
    private void processFile(File fileToAdd) throws IOException {
        // Check if the file exists and if it can be read
        if (!fileToAdd.canRead()) {
            this.writeMessage("Error while trying to reach the browsed file: it is probably on read-only mode.");
            return;
        }
        DropThatFile.models.File fileToUpload = new DropThatFile.models.File(1, fileToAdd.getName(), null, Date.from(Instant.now()), null);
        if(FilesJobs.Instance().sendFile(fileToAdd)){
            this.writeMessage(userAddFileNode(treeView_repository, icons, fileToAdd));
            //TODO: Send the File model Object to the database

        } else {
            this.writeMessage("An error occured while uploading the file.");
        }
    }

    /**
     * Inform the user of the result from his actions in the application
     */
    private void writeMessage(String msg) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtformatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.message_textArea.appendText(now.format(dtformatter) + "\n" + msg + "\n");
    }
}
