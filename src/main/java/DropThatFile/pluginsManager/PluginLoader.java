package DropThatFile.pluginsManager;

import DropThatFile.engines.LogManagement;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

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
    private int count = 0;

    //region GETTERS SETTERS
    public ArrayList<String> getPluginClassNames() {
        return pluginClassNames;
    }

    public void setPluginClassNames(ArrayList<String> pluginClassNames) {
        this.pluginClassNames = pluginClassNames;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }

    public ArrayList<URL> getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<URL> urls) {
        this.urls = urls;
    }

    public ArrayList<URLClassLoader> getUrlClassLoaders() {
        return urlClassLoaders;
    }

    public void setUrlClassLoaders(ArrayList<URLClassLoader> urlClassLoaders) {
        this.urlClassLoaders = urlClassLoaders;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    //endregion


    /**
     * Load a plugin from a jar files
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
        Class classToLoad = Class.forName (packageClassPath, true, urlClassLoaders.get(count));
        Method method = classToLoad.getDeclaredMethod("DropThatFile_Plugin_Launch", Stage.class);
        Object instance = classToLoad.newInstance();
        return (Stage) method.invoke(instance, new Stage());
    }

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

    private void getClassName(Object instance){
        pluginClassNames.add(instance.getClass().getSimpleName());
    }
}
