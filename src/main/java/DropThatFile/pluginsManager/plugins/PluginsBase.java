package DropThatFile.pluginsManager.plugins;

/**
 * Interface for plugins.
 * This interface is only vowed to be directly implemented into a plugin.
 * Its goal is to define a common behavior to all plugin interfaces
 *
 */
public interface PluginsBase {

	/**
	 * Get the libelle to display in the menus.
	 * @return String libelle.
	 */
	String getLibelle();
	
	/**
	 * Get plugin's category to further implementation of the menu in the right category.
	 * @return
	 */
	int getCategory();
}
