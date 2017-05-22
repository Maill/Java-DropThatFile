package DropThatFile.pluginsManager;

import DropThatFile.engines.annotations.Level;
import DropThatFile.engines.annotations._Todo;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Nicol on 21/03/2017.
 *
 * Plugin loader
 */
public class PluginLoader {
    public String simpleClassName_plugin = null;

    public PluginLoader(){ }

    /**
     * Load a plugin from a jar file
     * @param pluginJarPath absolute path to the jar
     * @param packageClassPath package class path
     * @throws Exception
     */
    public void load(String pluginJarPath, String packageClassPath) throws Exception {
        File file = new File(pluginJarPath);
        URL url = file.toURI().toURL();
        URLClassLoader child = new URLClassLoader (
                new URL[] {url},
                this.getClass().getClassLoader()
        );
        Class classToLoad = Class.forName (packageClassPath, true, child);
        Method method = classToLoad.getDeclaredMethod("DropThatFile_Plugin_Launch", Stage.class);
        Object instance = classToLoad.newInstance();
        Object result = method.invoke(instance, new Stage());
        //getClassName(instance);
    }

    private void getClassName(Object instance){
        simpleClassName_plugin = instance.getClass().getSimpleName();
    }
}
