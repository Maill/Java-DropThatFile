package DropThatFile.pluginsManager.plugins;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarFile;

/**
 * Class handling the plugins' loading and validation
 */
public class PluginsLoader {

	private String[] files;
	
	private ArrayList classStringPlugins;
	private ArrayList classIntPlugins;
	private ArrayList classIhmPlugins;
	
	/**
	 * Default constructor
	 *
	 */
	public PluginsLoader(){
		this.classIntPlugins = new ArrayList();
		this.classStringPlugins = new ArrayList();
		this.classIhmPlugins = new ArrayList();
	}
	
	/**
	 * CConstructor initializing the files (String) array to load.
	 * @param files Files (String) array to load.
	 */
	public PluginsLoader(String[] files){
		this();
		this.files = files;
	}
	
	/**
	 * Défini Files to load
	 * @param files
	 */
	public void setFiles(String[] files ){
		this.files = files;
	}
	
	/**
	 * Loading function for every String plugins
	 * @return StringPlugins collection
	 * @throws Exception if file == null or file.length == 0
	 */
	public StringPlugins[] loadAllStringPlugins() throws Exception {
		this.initializeLoader();
		StringPlugins[] tmpPlugins = new StringPlugins[this.classStringPlugins.size()];
		
		for(int index = 0 ; index < tmpPlugins.length; index ++ ){
			//New instance of the object in the list thanks to newInstance()
			//Casting it into StringPlugins.
			tmpPlugins[index] = (StringPlugins)((Class)this.classStringPlugins.get(index)).newInstance() ;
		}
		return tmpPlugins;
	}
	
	/**
	 * Loading function for every Int plugins
	 * @return IntPlugins collection
	 * @throws Exception if file == null or file.length == 0
	 */
	public IntPlugins[] loadAllIntPlugins() throws Exception {
		this.initializeLoader();
		IntPlugins[] tmpPlugins = new IntPlugins[this.classIntPlugins.size()];
		
		for(int index = 0 ; index < tmpPlugins.length; index ++ ){
			//New instance of the object in the list thanks to newInstance()
			//Casting it into IntPlugins.
			tmpPlugins[index] = (IntPlugins)((Class)this.classIntPlugins.get(index)).newInstance() ;
		}
		return tmpPlugins;
	}

	/**
	 * Loading function for every Ihm plugins
	 * @return IhmPlugins collection
	 * @throws Exception if file == null or file.length == 0
	 */
	public IhmPlugins[] loadAllIhmPlugins() throws Exception {
		this.initializeLoader();
		IhmPlugins[] tmpPlugins = new IhmPlugins[this.classIntPlugins.size()];

		for(int index = 0 ; index < tmpPlugins.length; index ++ ){
			//New instance of the object in the list thanks to newInstance()
			//Casting it into IhmPlugins.
			tmpPlugins[index] = (IhmPlugins)((Class)this.classIntPlugins.get(index)).newInstance() ;
		}
		return tmpPlugins;
	}
	
	private void initializeLoader() throws Exception{
		//Checking that the list of plugins is initialized
		if(this.files == null || this.files.length == 0 ){
			throw new Exception("No file specified");
		}

		//To avoid double loading of a plugin
		if(this.classIntPlugins.size() != 0 || this.classStringPlugins.size() != 0 || this.classIhmPlugins.size() != 0){
			return ;
		}
		
		File[] f = new File[this.files.length];
		//For loading the .jar into memory
		URLClassLoader loader;
		//For comparing Strings
		String tmp;
		//For the content of the .jar
		Enumeration enumeration;
		//To determine which interfaces are implemented
		Class tmpClass = null;
		
		for(int index = 0 ; index < f.length ; index ++ ){
			f[index] = new File(this.files[index]);
			
			if( !f[index].exists() ) {
				break;
			}
			
			URL u = f[index].toURI().toURL();
			//Creation of a new URLClassLoader to load the .jar which is outside of CLASSPATH
			loader = new URLClassLoader(new URL[] {u}); 
			
			//Loading .jar into memory
			JarFile jar = new JarFile(f[index].getAbsolutePath());
			
			//get .jar content
			enumeration = jar.entries();
			
			while(enumeration.hasMoreElements()){
				tmp = enumeration.nextElement().toString();
				//Checking for current file == .class
				if(tmp.length() > 6 && tmp.substring(tmp.length()-6).compareTo(".class") == 0) {
					tmp = tmp.substring(0,tmp.length()-6);
					tmp = tmp.replaceAll("/",".");
					tmpClass = Class.forName(tmp ,true,loader);
					
					for(int i = 0 ; i < tmpClass.getInterfaces().length; i ++ ){
						//A class must be in one category, but not more.
						//If it isn't the case: we place it, by default, into the first correct interface found.
						if(tmpClass.getInterfaces()[i].getName().toString().equals("tutoPlugins.pluginsManager.StringPlugins") ) {
							this.classStringPlugins.add(tmpClass);
						} else {
							if( tmpClass.getInterfaces()[i].getName().toString().equals("tutoPlugins.pluginsManager.IntPlugins") ) {
								this.classIntPlugins.add(tmpClass);
							} else {
								if( tmpClass.getInterfaces()[i].getName().toString().equals("tutoPlugins.pluginsManager.IhmPlugins") ) {
									this.classIhmPlugins.add(tmpClass);
								}
							}
						}
					}
				}
			}
		}
	}
}
