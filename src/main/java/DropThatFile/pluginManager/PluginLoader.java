package DropThatFile.pluginManager;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Olivier on 21/03/2017.
 *
 * Plugin loader
 */
@Deprecated
public class PluginLoader {
    private URL[] classLoaderUrls;
    private URLClassLoader urlClassLoader;
    private Class<?> beanClass;
    private Constructor<?> constructor;
    private Object pluginObject;
    private Method method;

    private File jarFile;
    private String classToLoad;
    private String methodToInvoke;

    /**
     * Invoke the specified method from the specified class from the specified jar file
     * @param jarFile Plugin File to load
     * @param classToLoad Class to instantiate from the jar file
     * @param methodToInvoke Method to invoke from the instantiated class
     * @throws Exception Throws 'MalformedURLException', 'ClassNotFoundException', 'InstantiationException',
     * and 'NoSuchMethodException'
     */
    public PluginLoader(File jarFile, String classToLoad, String methodToInvoke) throws Exception{
        this.jarFile = jarFile;
        this.classToLoad = classToLoad;
        this.methodToInvoke = methodToInvoke;

        // Getting the jar URL which contains target class
        classLoaderUrls = new URL[]{jarFile.toURI().toURL()};

        // Create a new URLClassLoader
        urlClassLoader = new URLClassLoader(classLoaderUrls);

        // Load the target class
        beanClass = urlClassLoader.loadClass(classToLoad);

        // Create a new instance from the loaded class
        constructor = beanClass.getConstructor();
        pluginObject = constructor.newInstance();

        method = beanClass.getMethod(methodToInvoke);
    }

    /**
     * Invoke the method from the constructor of PluginLoader
     * @param param1 First parameter of the called function
     * @throws Exception Throws 'MalformedURLException', 'ClassNotFoundException', 'IllegalAccessException',
     * 'InvocationTargetException', 'InstantiationException', and 'NoSuchMethodException'
     */
    public Object invokeMethod(Object param1) throws Exception{
        method = beanClass.getMethod(methodToInvoke, param1.getClass());
        return method.invoke(pluginObject, param1);
    }

    /**
     * Invoke the method from the constructor of PluginLoader
     * @param param1 First parameter of the called function
     * @param param2 Second parameter of the called function
     * @throws Exception Throws 'MalformedURLException', 'ClassNotFoundException', 'IllegalAccessException',
     * 'InvocationTargetException', 'InstantiationException', and 'NoSuchMethodException'
     */
    public Object invokeMethod(Object param1, Object param2) throws Exception{
        method = beanClass.getMethod(methodToInvoke, param1.getClass(), param2.getClass());
        return method.invoke(pluginObject, param1, param2);
    }

    /**
     * Invoke the method from the constructor of PluginLoader
     * @param param1 First parameter of the called function
     * @param param2 Second parameter of the called function
     * @param param3 Third parameter of the called function
     * @throws Exception Throws 'MalformedURLException', 'ClassNotFoundException', 'IllegalAccessException',
     * 'InvocationTargetException', 'InstantiationException', and 'NoSuchMethodException'
     */
    public Object invokeMethod(Object param1, Object param2, Object param3) throws Exception{
        method = beanClass.getMethod(methodToInvoke, param1.getClass(), param2.getClass(), param3.getClass());
        return method.invoke(pluginObject, param1, param2, param3);
    }
}
