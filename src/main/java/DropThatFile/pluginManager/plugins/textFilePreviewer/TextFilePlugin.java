package DropThatFile.pluginManager.plugins.textFilePreviewer;

import DropThatFile.pluginManager.IPlugin;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


/**
 * Created by Olivier on 02/05/2017.
 */
public class TextFilePlugin extends Plugin {
    public TextFilePlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class TextFilePreviewer implements IPlugin {
        @Override
        public String getFileContent(File fileToPreview) throws IOException {
            StringBuilder contentBuilder = new StringBuilder();
            try(BufferedReader in = new BufferedReader(new FileReader(fileToPreview.getAbsolutePath()))) {
                String str;
                while ((str = in.readLine()) != null) {
                    contentBuilder.append(str);
                }
                return contentBuilder.toString();
            } catch (IOException ex) {
                ex.getStackTrace();
                return null;
            }
        }
    }
}