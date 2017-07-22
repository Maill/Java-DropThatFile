package DropThatFile.pluginManager;

import javafx.scene.control.TreeItem;
import ro.fortsoft.pf4j.JarPluginManager;
import ro.fortsoft.pf4j.PluginManager;
import ro.fortsoft.pf4j.PluginWrapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by Olivier on 02/05/2017.
 */
public class Expander {
    private final PluginManager pluginManager = new JarPluginManager();
    // Retrieves the extensions for IPlugin extension point after plugins' launch
    private List<IPlugin> previewers;
    // Retrieves extensions from classpath (non plugin) after plugins' launch
    private Set<String> extensionClassNames;
    // Retrieves extensions for each started plugin after plugins' launch
    private List<PluginWrapper> startedPlugins;

    //region Plugin initialization
    /**
     * Load all plugins implementing the IPlugin interface and extending the P4J Plugin class.
     * Default : enable
     */
    public void loadAll(){
        pluginManager.loadPlugins();
    }

    /**
     * Disable an enabled plugin
     * @param pluginPropertyId Plugin id in its plugin.properties file
     */
    public void disablePlugin(String pluginPropertyId){
        pluginManager.disablePlugin(pluginPropertyId);
    }

    /**
     * Enable a disabled plugin
     * @param pluginPropertyId Plugin id in its plugin.properties file
     */
    public void enablePlugin(String pluginPropertyId){
        pluginManager.enablePlugin(pluginPropertyId);
    }

    /**
     * Start all enabled plugins
     */
    public void startAll(){
        pluginManager.startPlugins();
    }

    /**
     * Stop all plugins
     */
    public void stopAll(){
        pluginManager.stopPlugins();
    }
    //endregion

    /**
     * Plugins setup
     */
    public void loadFilePreviewers() {
        // Start loaded plugins
        loadAll();
        startAll();

        // Retrieves the extensions for IPlugin extension point
        previewers = pluginManager.getExtensions(IPlugin.class);
        // Retrieves extensions from classpath (non plugin)
        extensionClassNames = pluginManager.getExtensionClassNames(null);
        // Retrieves extensions for each started plugin
        startedPlugins = pluginManager.getStartedPlugins();
    }

    /**
     * Conjunction method for the loaded previewer plugins
     * @param selectedNode Selected TreeItem File in a TreeView File
     * @return String content to display to the user
     */
    public String getFilePreview(TreeItem<File> selectedNode){
        File selectedFile = selectedNode.getValue();
        String selectedFileName = selectedNode.getValue().getName();
        for (IPlugin previewer : previewers) {
            try {
                String className = previewer.getClass().getSimpleName();
                if(selectedFileName.endsWith(".xlsx")) {
                    if(className.equals("ExcelFilePreviewer")) {
                        return previewer.getFileContent(selectedFile);
                    }
                } else {
                    if(className.equals("TextFilePreviewer")) {
                        return previewer.getFileContent(selectedFile);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
