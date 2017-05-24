package DropThatFile.engines;

import DropThatFile.engines.LogManagement;
import org.apache.log4j.Logger;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Travail on 24/05/2017.
 */
public final class TextFilePreviewer {
    Logger log = LogManagement.getInstanceLogger(this);

    public void getContent(TextArea textArea) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("<Filename>"));
        String line;
        try {
            while((line = in.readLine()) != null) {
                textArea.setText(line);
            }
        } catch(Exception ex){
            log.error("Erreur lors de la lecture du fichier texte.\nMessage :\n" + ex);
        } finally {
            in.close();
        }
    }
}
