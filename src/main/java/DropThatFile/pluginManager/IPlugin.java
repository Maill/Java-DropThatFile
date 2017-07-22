package DropThatFile.pluginManager;

import ro.fortsoft.pf4j.ExtensionPoint;

import java.io.File;
import java.io.IOException;

/**
 * Created by Olivier on 02/05/2017.
 */
public interface IPlugin extends ExtensionPoint {

    String getFileContent(File fileToPreview) throws IOException;

}
