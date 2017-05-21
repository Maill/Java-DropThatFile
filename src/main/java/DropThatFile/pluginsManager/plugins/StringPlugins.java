package DropThatFile.pluginsManager.plugins;

/**
 * This interface is strictly vowed to be used by plugins, but not by internal application classes
 */
public interface StringPlugins extends PluginsBase {

	/**
	 * Principal process function of String plugins
	 * @param ini Initial String
	 * @return Processed String
	 */
	String actionOnString(String ini);
	
}
