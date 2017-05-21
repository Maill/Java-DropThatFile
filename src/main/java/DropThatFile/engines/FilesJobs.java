package DropThatFile.engines;

import DropThatFile.controllers.HomeController;
import javafx.scene.control.*;
import javafx.scene.control.ButtonType;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Nicol on 22/03/2017.
 */
public class FilesJobs {

    private static final String tmpFilePath = System.getenv("APPDATA") + "\\DropThatFile\\tmpfiles";

    static {
        File f = new File(tmpFilePath);
        if (!f.exists() || !f.isDirectory()) {
            f.mkdirs();
        }
    }

    // Crypte et envoie le fichier au serveur de stockage
    public static void encryptFile(DropThatFile.models.File file, List<java.io.File> filesPath) throws ZipException {
        // Zip name
        String fileNameWithoutExt = file.getName().replaceFirst("[.][^.]+$", "");

        if(fileAlreadyExists(fileNameWithoutExt)){
            return;
        }

        // Zip location
        ZipFile zipFile = new ZipFile(tmpFilePath + "\\" + fileNameWithoutExt + ".zip");

        ArrayList<File> filesToAdd = new ArrayList();
        for (File filePath : filesPath) {
            filesToAdd.add(new File(filePath.getAbsolutePath()));
            System.out.println("File AbsolutePath : " + filePath.getAbsolutePath());
        }

        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // set compression method to deflate compression

        //DEFLATE_LEVEL_FASTEST     - Lowest compression level but higher speed of compression
        //DEFLATE_LEVEL_FAST        - Low compression level but higher speed of compression
        //DEFLATE_LEVEL_NORMAL  - Optimal balance between compression level/speed
        //DEFLATE_LEVEL_MAXIMUM     - High compression level with a compromise of speed
        //DEFLATE_LEVEL_ULTRA       - Highest compression level but low speed
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        parameters.setEncryptFiles(true);

        parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);

        parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);

        parameters.setPassword(HomeController.password);

        try{
            zipFile.addFiles(filesToAdd, parameters);
        } catch(ZipException ex){
            System.out.println("ZipException : " + ex.getMessage());
        }

        System.out.println("\nZipFile : " + zipFile.getFile().toString());

        sendFileToServer(zipFile.getFile().getPath());
    }

    //SFTP
    private static boolean sendFileToServer(String pathFileToSend){
        System.out.println("\nPathFileToSend : " + pathFileToSend);
        return true;
    }

    private static boolean fileAlreadyExists(String fileNameWithoutExt){
        File fileToOverwrite = new File(tmpFilePath + "\\" + fileNameWithoutExt + ".zip");
        Alert alertOverwriteFile = new Alert(Alert.AlertType.CONFIRMATION);
        Alert alertFileCantBeDeleted = new Alert(Alert.AlertType.ERROR);
        if (fileToOverwrite.exists()){
            if (fileToOverwrite.isFile() && fileToOverwrite.canExecute()){
                alertOverwriteFile.setTitle("Confirmation Dialog");
                alertOverwriteFile.setHeaderText("File already exists.");
                alertOverwriteFile.setContentText("File already exists at the output destination. Overwrite it?");

                Optional<ButtonType> result = alertOverwriteFile.showAndWait();
                if (result.get().equals(ButtonType.OK)){
                    fileToOverwrite.delete();
                    return false;
                } else {
                    return true;
                }
            } else {
                alertFileCantBeDeleted.setTitle("Error Dialog");
                alertFileCantBeDeleted.setHeaderText("Operation aborted - File can not be deleted.");
                alertFileCantBeDeleted.setContentText("Please check if the file does exist, if it's really a file and if you're allowed to execute/delete it.");
                alertFileCantBeDeleted.showAndWait();
                return true;
            }
        }
        return false;
    }
}
