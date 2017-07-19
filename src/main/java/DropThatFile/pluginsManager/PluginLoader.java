package DropThatFile.pluginsManager;

import DropThatFile.engines.LogManagement;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nicol on 21/03/2017.
 *
 * Plugin loader
 */
public final class PluginLoader {
    private Logger log = LogManagement.getInstanceLogger(this);
    private ArrayList<String> pluginClassNames = new ArrayList<>();
    private ArrayList<File> files = new ArrayList<>();
    private ArrayList<URL> urls = new ArrayList<>();
    private ArrayList<URLClassLoader> urlClassLoaders = new ArrayList<>();
    private HashMap<String, Stage> pluginStages = new HashMap<>();
    private int count = 0;

    /**
     * Load a plugin from a jar file
     * @param pluginJarPath absolute path to the jar
     * @param packageClassPath package class path
     * @throws Exception
     */
    public Stage load(String pluginJarPath, String packageClassPath) throws Exception {
        files.add(new File(pluginJarPath));
        urls.add(new URL(files.get(count).toURI().toURL().toString()));
        urlClassLoaders.add(new URLClassLoader(
                new URL[] {urls.get(count)},
                this.getClass().getClassLoader()
        ));

        Class classToLoad = Class.forName(packageClassPath, true, urlClassLoaders.get(count));
        Method method = null;
        try{
            method = classToLoad.getDeclaredMethod("DropThatFile_Start", Stage.class);
        }
        catch (NullPointerException ex){ // If no such method is found
            log.warn(ex.getMessage(), ex);
            return new Stage();
        }
        Object instance = classToLoad.newInstance();
        String className = getClassName(instance);
        Stage pluginStage = getPluginStage(method, instance);

        pluginClassNames.add(className);
        pluginStages.put(className, pluginStage);


        return pluginStages.get(className);
    }

    /**
     * Unload loaded plugins
     */
    public void unloadAll() {
        try {
            urlClassLoaders.clear();
            urls.clear();
            files.clear();
            pluginClassNames.clear();
            count = 0;
        }catch(NullPointerException ex){
            log.warn(ex.getMessage(), ex);
        }
    }

    private Stage getPluginStage(Method method, Object instance) throws InvocationTargetException, IllegalAccessException {
        return (Stage)method.invoke(instance, new Stage());
    }

    private String getClassName(Object instance){
        return instance.getClass().getSimpleName();
    }
}
