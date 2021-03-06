package DropThatFile.controllers;

import DropThatFile.engines.APIData.APIModels.APIFile;
import DropThatFile.engines.APIData.APIModels.APIGroup;
import DropThatFile.engines.FilesJobs;
import DropThatFile.engines.LogManagement;
import DropThatFile.engines.annotations.Translate;
import DropThatFile.engines.windowsManager.WindowsHandler;
import DropThatFile.models.Group;
import DropThatFile.pluginManager.Expander;
import DropThatFile.pluginManager.PluginLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.image.ImageView;
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
import java.net.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import static DropThatFile.engines.windowsManager.TreeViewer.*;
import static DropThatFile.GlobalVariables.*;

/**
 * Created by Olivier on 02/05/2017.
 */
public class HomeController extends AnchorPane implements Initializable {
    //region FXML
    @FXML private TabPane repositories_tabPane;

    @FXML
    @Translate(translation = "Mon dépôt")
    private Tab myRepository_tab;

    @FXML
    @Translate(translation = "Dépôts")
    private Tab home_tab;

    @FXML private Button button_archiveBrowse;

    @FXML private Button button_browse;

    @FXML private Button button_synchronize;

    @FXML
    public TreeView<File> treeView_repository;

    @FXML
    @Translate(translation = "Actualisation automatique")
    private CheckBox checkBox_autoRefresh;

    @FXML private TextArea message_textArea;

    @FXML private TextArea preview_textArea;

    @FXML private TextField textField_folderName;

    @FXML
    @Translate(translation = "Nom du dossier")
    private Label label_folderName;

    @FXML
    @Translate(translation = "Créer")
    private Button createFolder_button;

    @FXML
    @Translate(translation = "Suppr.")
    private Button removeFolder_button;

    //@FXML
    //private ImageView imageView_flagEN;

    @FXML
    private ImageView imageView_flagFR;

    @FXML
    public ListView<File> listView_plugins;

    @FXML
    private Button button_addPlugin;

    @FXML
    private Button button_removePlugin;

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
            new Image(getClass().getResourceAsStream("/images/folder.png")),
            new Image(getClass().getResourceAsStream("/images/file.png")),
            new Image(getClass().getResourceAsStream("/images/zip.png")),
            new Image(getClass().getResourceAsStream("/images/text.png")),
            new Image(getClass().getResourceAsStream("/images/image.png")),
            new Image(getClass().getResourceAsStream("/images/word.png")),
            new Image(getClass().getResourceAsStream("/images/excel.png"))
    };

    // Log4j instance
    private final Logger log = LogManagement.getInstanceLogger(this);

    // Launch the default web browser
    private Desktop desktop = Desktop.getDesktop();

    private Stage stage = new Stage();

    private WindowsHandler windowsHandler = new WindowsHandler(stage);

    public static PluginLoader pluginLoader;

    private Expander pluginExpander = new Expander();

    public static String currentFilePassword = null;
    public static String currentFileName = null;
    public static String currentFileDescription = null;

    private DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Download all needed files
        FilesJobs.Instance().downloadUserFiles();

        ArrayList<ImageView> langFlags = new ArrayList<>();
        //langFlags.add(imageView_flagEN);
        langFlags.add(imageView_flagFR);
        // Set translation annotation
        windowsHandler.languageListening(this, langFlags);

        // Set the TreeView
        buildTreeView(treeView_repository, icons, contextMenu, checkBox_autoRefresh, currentUserRepoPath);
        // Set a bunch of graphic controls and events
        setAllControls();
        // Link the TreeView to the groups of the user too
        setUserGroupTabPanes();

        // Set the plugin ListView
        setPluginListView();
        // Set the pluginsArrayList for previewing files
        pluginExpander.loadFilePreviewers();
    }

    /**
     * Create a Tab in the PaneTab for each group where the user is a member of.
     */
    private void setUserGroupTabPanes(){
        try {
            HashMap<Integer, Group> groupsOfCurrentUser = APIGroup.Instance().getGroupsForUser();
            if(groupsOfCurrentUser.isEmpty()) return;
            ArrayList<Integer> groupsId = new ArrayList<>(groupsOfCurrentUser.keySet());

            int i = 0;
            for (Group group : groupsOfCurrentUser.values()) {
                if(!currentGroups.contains(group)) currentGroups.add(group);

                String groupName = currentGroups.get(i).getName();
                String currentGroupPath = groupRepoMainPath.concat(groupName) + "\\";

                Tab groupTab = new Tab(groupName);
                groupTab.setClosable(false);
                groupTab.setOnSelectionChanged(e1 -> {
                    FilesJobs.Instance().downloadGroupFiles(groupsId);
                    setNodes(treeView_repository, icons, currentGroupPath);

                    button_synchronize.setOnAction(e2 -> {
                        FilesJobs.Instance().downloadGroupFiles(groupsId);
                        setNodes(treeView_repository, icons, currentGroupPath);
                    });
                });

                repositories_tabPane.getTabs().add(groupTab);

                i++;
            }
        } catch(IndexOutOfBoundsException ex){
            ex.printStackTrace();
        } finally {
            myRepository_tab.setOnSelectionChanged(e -> {
                FilesJobs.Instance().downloadUserFiles();
                setNodes(treeView_repository, icons, currentUserRepoPath);

                button_synchronize.setOnAction(e2 -> {
                    FilesJobs.Instance().downloadUserFiles();
                    setNodes(treeView_repository, icons, currentUserRepoPath);
                });
            });
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
        contextMenu.getItems().get(0).setOnAction(e ->
            alertFilePreview(
                    pluginExpander.getFilePreview(treeView_repository.getSelectionModel().getSelectedItem())
            )
        );
        // Open MenuItem
        contextMenu.getItems().get(1).setOnAction(e -> this.writeMessage(openFile(treeView_repository)));
        // Add folder MenuItem
        contextMenu.getItems().get(2).setOnAction(e ->
                this.writeMessage(userAddFolderNode(treeView_repository, icons, "New folder")));
        // Delete MenuItem
        contextMenu.getItems().get(3).setOnAction(e -> this.writeMessage(alertDeletion(treeView_repository)));

        // Button events
        createFolder_button.setOnAction(e -> this.writeMessage(userAddFolderNode(treeView_repository, icons, textField_folderName.getText())));
        removeFolder_button.setOnAction(e -> this.writeMessage(alertDeletion(treeView_repository)));
    }

    /**
     * Set the ListView for the plugins to be added
     */
    private void setPluginListView(){
        listView_plugins.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        listView_plugins.setCellFactory(listView -> new ListCell<File>() {
            @Override
            public void updateItem(File filePath, boolean empty) {
                super.updateItem(filePath, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(filePath.getName());
                    setGraphic(null);
                }
            }
        });

        setAddPluginButton();
        setRemovePluginButton();
    }

    /**
     * Set the '+' button to add a plugin
     */
    private void setAddPluginButton(){
        final FileChooser jarChooser = new FileChooser();
        jarChooser.setTitle("Choose a jar plugin");
        jarChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Jar files", "*.jar"));
        Stage jarChooserStage = new Stage();

        button_addPlugin.setOnAction(e -> {
            File pluginToAdd = jarChooser.showOpenDialog(jarChooserStage);
            if (pluginToAdd != null) {
                // Plugin implementation
                try {
                    for (File file : listView_plugins.getItems()) {
                        if(pluginToAdd.getName().equals(file.getName())){
                            listView_plugins.getItems().remove(listView_plugins.getSelectionModel().getSelectedItem());
                            break;
                        }
                    }
                    /*String test = "DropThatFile.pluginManager.plugins.excelFilePlugin.ExcelFilePlugin$ExcelFilePreviewer";
                    pluginLoader = new PluginLoader(
                            pluginToAdd, test, "getFileContent"
                    );*/

                    ObservableList<File> newFile = FXCollections.observableArrayList(pluginToAdd);
                    listView_plugins.setItems(newFile);
                    this.writeMessage("Plugin successfully loaded.");
                } catch (Exception e1) {
                    e1.printStackTrace();
                    this.writeMessage("Failed to load plugin.");
                }
            }
        });
    }

    /**
     * Set the '-' button to remove a plugin
     */
    private void setRemovePluginButton(){
        button_removePlugin.setOnAction(e -> {
            // Retrieve current selected item
            File selectedFile =  listView_plugins.getSelectionModel().getSelectedItem();

            if (listView_plugins.getSelectionModel().isEmpty() || selectedFile == null) {
                this.writeMessage("Please select a plugin.");
            } else {
                listView_plugins.getItems().remove(selectedFile);
                this.writeMessage("Plugin successfully unloaded.");
            }
        });
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
            currentFileName = zipNameTextField.getText();
            currentFilePassword = zipPasswordTextField.getText();
            currentFileDescription = zipDescriptionTextField.getText();

            if (currentFileName != null && currentFilePassword != null) {
                if(currentFileName.length() >= 1 && currentFilePassword.length() >= 1){
                    if(zipDescriptionTextField.getText().trim().isEmpty())
                        currentFileDescription = "No description available";
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

        Scene dialogScene = new Scene(root, 600, 300);
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
            else if (currentFileName == null || currentFilePassword == null){
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
                    } finally {
                        currentFileName = null;
                        currentFilePassword = null;
                        currentFileDescription = null;
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

        // Add selected files into an encrypted zip file
        try {
            TreeItem<File> selectedFolder = treeView_repository.getSelectionModel().getSelectedItem();

            boolean isForUser = myRepository_tab.isSelected();

            if(FilesJobs.Instance().sendEncryptedArchive(files, selectedFolder, isForUser)){
                if(isForUser){
                    APIFile.Instance().addArchiveUser(
                            FilesJobs.currentArchiveName,
                            HomeController.currentFileDescription,
                            HomeController.currentFilePassword
                    );
                } else {
                    APIFile.Instance().addArchiveGroup(
                            FilesJobs.currentArchiveName,
                            HomeController.currentFileDescription,
                            HomeController.currentFilePassword,
                            repositories_tabPane.getSelectionModel().getSelectedItem().getText()
                    );
                }

                this.writeMessage("Archive successfully uploaded.");
                setNodes(treeView_repository, icons, currentUserRepoPath);
                return;
            }
            this.writeMessage("Failed to upload archive.");

        } catch (ZipException ex) {
            ex.printStackTrace();
            log.error("Erreur en tentant de créer une archive encryptée.\nMessage : \n" + ex.getMessage());
        } finally {
            currentFileName = null;
            currentFilePassword = null;
            currentFileDescription = null;
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
        boolean isForUser = myRepository_tab.isSelected();
        TreeItem<File> selectedItem = treeView_repository.getSelectionModel().getSelectedItem();
        File newFile = new File(selectedItem.getValue().getAbsolutePath() + "\\" + fileToAdd.getName());
        Files.copy(fileToAdd.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        if(FilesJobs.Instance().sendFileToServer(newFile, isForUser)){
            if(isForUser){
                APIFile.Instance().addFileUser(
                        newFile.getParent() + "\\" + newFile.getName(),
                        HomeController.currentFileDescription
                );
            } else {
                APIFile.Instance().addFileGroup(
                        newFile.getParent() + "\\" + newFile.getName(),
                        HomeController.currentFileDescription,
                        repositories_tabPane.getSelectionModel().getSelectedItem().getText()
                );
            }

            this.writeMessage(userAddFileNode(treeView_repository, icons, newFile));
            setNodes(treeView_repository, icons, currentUserRepoPath);
        } else {
            this.writeMessage("An error occurred while uploading the file.");
        }

        currentFileName = null;
        currentFilePassword = null;
        currentFileDescription = null;
    }

    /**
     * Create an alert that demands confirmation from the user to delete the selected node and its potential children
     */
    private String alertDeletion(TreeView<File> treeView){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirming dialog");
        alert.setHeaderText("Delete this?");
        alert.setContentText("Are you sure you want to delete this?");

        boolean isForUser = myRepository_tab.isSelected();
        TreeItem<File> selectedNode = treeView_repository.getSelectionModel().getSelectedItem();

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            if(FilesJobs.Instance().deleteFile(selectedNode.getValue(), isForUser)){
                if(selectedNode.getValue().delete()){
                    return userRemoveNode(treeView_repository);
                } else {
                    return "Element deletion aborted.";
                }
            } else {
                return "Element deletion aborted.";
            }
        } else {
            return "Element deletion aborted.";
        }
    }

    /**
     * Create an alert on primary click on the preview MenuItem.
     * Preview the content of a file
     * @return
     */
    private void alertFilePreview(String fileContent){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Preview Dialog");
        alert.setHeaderText("This is the content of the file");

        TextArea textArea =
                new TextArea(fileContent != null ? fileContent : "INFORMATION: No valid content found in this file.");
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane content = new GridPane();
        content.setMaxWidth(Double.MAX_VALUE);
        content.add(textArea, 0, 0);

        // Set expandable content into the dialog pane.
        alert.getDialogPane().setExpandableContent(content);
        alert.getDialogPane().setExpanded(true);

        alert.showAndWait();
    }

    /**
     * Inform the user of the result from his actions in the application
     */
    private void writeMessage(String msg) {
        LocalDateTime now = LocalDateTime.now();
        message_textArea.appendText("At " + now.format(dtFormatter) + "\n" + msg + "\n");
    }
    //region
    public void setLabelFolderName(String str){
        label_folderName.setText(str);
    }

    public void setButtonCreateFolder(String str){
        createFolder_button.setText(str);
    }

    public void setButtonRemoveFolder(String str){
        removeFolder_button.setText(str);
    }

    public void setTabMyRepository(String str){
        myRepository_tab.setText(str);
    }

    public void setTabHome(String str){
        home_tab.setText(str);
    }

    public void setCheckBoxAutoRefresh(String str){
        checkBox_autoRefresh.setText(str);
    }
    //endregion
}
