package DropThatFile.engines;

import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Nicol on 21/03/2017.
 *
 * Plugin loader
 */
public class PluginLoader {
    public PluginLoader(){
    }

    public void load(String path, String packageClassPath) throws Exception {
        File file = new File(path);
        URL url = file.toURI().toURL();
        URLClassLoader child = new URLClassLoader (
                new URL[] {url},
                this.getClass().getClassLoader()
        );
        Class classToLoad = Class.forName (packageClassPath, true, child);
        Method[] test  = classToLoad.getMethods();
        Method method = classToLoad.getDeclaredMethod("start", Stage.class);
        Object instance = classToLoad.newInstance();
        Object result = method.invoke(instance, new Stage());
    }
}
