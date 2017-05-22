package DropThatFile.controllers;

import DropThatFile.engines.FilesJobs;
import DropThatFile.engines.LogManagement;
import DropThatFile.engines.windowsManager.TreeViewRepository;
import DropThatFile.engines.windowsManager.WindowsHandler;
import DropThatFile.engines.windowsManager.forms.HomeForm;
import DropThatFile.pluginsManager.PluginLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseEvent;
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
import java.util.*;
import java.util.List;

/**
 * Created by Travail on 02/05/2017.
 */
public class HomeController extends AnchorPane implements Initializable {
    //region FXML
    @FXML
    /**
     * Open a hypertext link
     */
    public void openLink() throws URISyntaxException, IOException {
        if(!desktop.isSupported(Desktop.Action.BROWSE)) return;
            desktop.browse(new URI("www.google.com"));
    }

    @FXML
    private Button browse;

    @FXML
    private TreeView treeView_repository;

    @FXML
    private TextArea message_textArea;

    @FXML
    private TextField item_textField;

    @FXML
    private Button item_add;

    @FXML
    private Button item_remove;

    @FXML
    private Button add_plugin;

    @FXML
    private Button clear_plugin;

    @FXML
    private StackPane stackPane_plugin;
    //endregion

    private final Logger log = LogManagement.getInstanceLogger(this);

    private Desktop desktop = Desktop.getDesktop();

    private Stage stage = new Stage();

    private WindowsHandler windowsHandler = new WindowsHandler(stage);

    private HomeForm application;

    public static String password = null;
    public static String zipName = null;
    private String jarPath = "C:\\Users\\Travail\\IdeaProjects\\SkinLoader\\out\\artifacts\\SkinLoader_jar\\SkinLoader.jar";
    private String packageClassPath = "com.company.CssLoader";

    public void setApp(HomeForm application){
        this.application = application;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setBrowseButton();
        setTreeView_repository(treeView_repository);
        initializePluginLoader();
    }

    private void initializePluginLoader(){
        add_plugin.setOnMouseClicked((MouseEvent event) -> {
            PluginLoader pluginLoader = new PluginLoader();
            try {
                pluginLoader.load(jarPath, packageClassPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void modalPassword(){
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(windowsHandler.getJfxStage());

        PasswordField passwordTextField = new PasswordField();
        TextField zipNameTextField = new TextField();
        passwordTextField.setPromptText("Password");
        zipNameTextField.setPromptText("Zip name");
        Button btnValidate = new Button("Continue");

        HBox dialogHbox = new HBox(5);
        dialogHbox.getChildren().add(new Text("Type a fileName for the zip, and a password for your files: "));
        dialogHbox.getChildren().add(btnValidate);
        VBox dialogVox = new VBox(5);
        dialogVox.getChildren().add(zipNameTextField);
        dialogVox.getChildren().add(passwordTextField);

        btnValidate.setOnMouseClicked((MouseEvent event) -> {
            zipName = zipNameTextField.getText();
            password = passwordTextField.getText();
            dialog.close();
        });

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20)); // space between elements and window border
        root.setTop(dialogHbox);
        root.setCenter(dialogVox);
        root.setBottom(btnValidate);

        Scene dialogScene = new Scene(root, 400, 200);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    private void setBrowseButton() {
        final FileChooser fileChooser = new FileChooser();
        Stage fileChooserStage = new Stage();
        // set initial directory of the "browse window" to the user's dir
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Select files");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        browse.setOnAction((ActionEvent e) -> {
            modalPassword();
            // Retrieve current selected item
            TreeItem selectedTreeItem = (TreeItem)treeView_repository.getSelectionModel().getSelectedItem();
            // If there's no selected item || if selected item is the root item || or is null
            if (treeView_repository.getSelectionModel().isEmpty() || selectedTreeItem.getValue().equals("root") || selectedTreeItem.getValue() == null) {
                this.writeMessage("Please select a proper directory.");
                return;
            }
            if (zipName == null || password == null){
                this.writeMessage("Please set a proper password and zip name.");
                return;
            }
            List<File> list = fileChooser.showOpenMultipleDialog(fileChooserStage);
            if (list != null) {
                FilesJobs filesJobs = new FilesJobs();
                try {
                    processFiles(filesJobs, list);
                } catch(IOException | ZipException ex){
                    this.writeMessage("Error while trying to reach browsed file."  + ex.getMessage());
                    log.trace("CUSTOM LOG - Error while trying to reach browsed file: \n" + ex.getMessage(), ex);
                }
            }
        });
    }

    private void processFiles(FilesJobs filesjobs, List<File> files) throws IOException, ZipException {
        // Check if the file exists and if it can be read
        for (File currentFile : files) {
            if (!currentFile.canRead()) {
                this.writeMessage("Error while trying to reach one of the browsed files.");
                return;
            }
        }
        DropThatFile.models.File fileUpload = new DropThatFile.models.File(1, zipName, password, Date.from(Instant.now()), "TEST");
        filesjobs.encryptFile(fileUpload, files);
    }

    //region TreeView
    /**
     * Configuration of the TreeView
     * @param treeView fx:id of a TreeView element
     */
    private void setTreeView_repository(TreeView treeView){
        TreeViewRepository repo = new TreeViewRepository();
        ArrayList<TreeItem> all = repo.getAll();
        TreeItem rootItem = new TreeItem("root");
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
        item_add.setOnAction(event -> addItem(item_textField.getText()));
        // Remove the selected item
        item_remove.setOnAction(event -> removeItem());
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
     */
    private void addItem(String value)
    {
        if (value == null || value.trim().equals(""))
        {
            this.writeMessage("Directory name cannot be empty.");
            return;
        }

        TreeItem parent = (TreeItem) treeView_repository.getSelectionModel().getSelectedItem();

        if (parent == null)
        {
            this.writeMessage("Select a directory to add this directory to.");
            return;
        }
        if (((TreeItem) treeView_repository.getSelectionModel().getSelectedItem()).getValue().equals("root")){
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
     */
    private void removeItem()
    {
        TreeItem item = (TreeItem) treeView_repository.getSelectionModel().getSelectedItem();

        if (item == null)
        {
            this.writeMessage("Select a directory to remove.");
            return;
        }

        TreeItem parent = item.getParent();
        if (parent == null || item.getValue().equals("My Files") || item.getValue().equals("Shared Files"))
        {
            this.writeMessage("Cannot remove main directories.");
        } else
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
