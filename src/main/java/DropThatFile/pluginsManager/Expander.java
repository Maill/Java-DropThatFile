package DropThatFile.pluginsManager;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * This class is unfinished
 */
public class Expander {
    private File pluginFile;
    private URL pluginURL;
    private String pluginAbsolutePath;
    private String pluginPath;
    private String pluginName;
    private Method method;
    private URLClassLoader classLoader;

    public Expander(File pluginFile) throws MalformedURLException {
        this.pluginFile = pluginFile;
        this.pluginURL = pluginFile.toURI().toURL();
        this.pluginAbsolutePath = pluginFile.getAbsolutePath();
        this.pluginPath = pluginFile.getPath();
        this.pluginName = pluginFile.getName();
    }

    /**
     * Load plugins
     */
    public void load(){
        try {
            this.classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
            this.method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            this.method.setAccessible(true);
            this.method.invoke(this.classLoader, this.pluginURL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Unload plugins
     */
    public void unload(){
        try {
            this.classLoader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
