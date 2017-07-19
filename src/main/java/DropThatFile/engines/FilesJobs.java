package DropThatFile.engines;

import DropThatFile.GlobalVariables;
import DropThatFile.controllers.HomeController;
import DropThatFile.engines.annotations.Level;
import DropThatFile.engines.annotations._Todo;
import javafx.scene.control.*;
import javafx.scene.control.ButtonType;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

/**
 * Created by Nicol on 22/03/2017.
 */
public class FilesJobs {
    //region init
    static {
        File f = new File(GlobalVariables.outputZipPath);
        if (!f.exists() || !f.isDirectory()) {
            f.mkdirs();
        }
    }
    //endregion

    /**
     * Send the file to the storage server
     * @param file DropThatFile.models.File file model describing the file
     */
    public static void sendFiles(File file) {
        // File future location
        if (!new File(GlobalVariables.outputZipPath + "\\" + file.getName()).exists()){
            new File(GlobalVariables.outputZipPath + "\\" + file.getName());
            //sendFileToServer("blabla");
        } else {
            //TODO: liste de fichiers qui n'ont pas envoyés

        }
    }

    /**
     * Encrypt and send the archive to the storage server
     * @param fileModel DropThatFile.models.File file model describing the actual file
     * @param filesInArchive List of files to add in the archive
     */
    public static void sendEncryptedArchive(DropThatFile.models.File fileModel, List<File> filesInArchive) throws ZipException {
        // Zip name
        String fileNameWithoutExt = fileModel.getName().replaceFirst("[.][^.]+$", "");

        if(fileAlreadyExists(fileNameWithoutExt)) return;

        // Zip future location
        ZipFile zipFile = new ZipFile(GlobalVariables.outputZipPath + "\\" + fileNameWithoutExt + ".zip");

        ArrayList<File> filesToAdd = new ArrayList<>();
        for (File f : filesInArchive) {
            filesToAdd.add(new File(f.getAbsolutePath()));
        }

        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // set compression method to deflate compression

        //DEFLATE_LEVEL_NORMAL  - Optimal balance between compression level/speed
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        parameters.setEncryptFiles(true);
        parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
        parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
        parameters.setPassword(HomeController.zipPassword);

        try{
            zipFile.addFiles(filesToAdd, parameters);
        } catch(ZipException ex){
            System.out.println("ZipException : " + ex.getMessage());
        }

        //sendFileToServer(zipFile.getFile().getPath());
    }

    /**
     * Submit the file, found in the path' parameter, to the storage server <Protocol SFTP>
     * @param pathFileToSend Path of the file to send
     */
    @_Todo(level = Level.EVOLUTION, comment = "Pouvoir envoyer les fichiers au serveur de stockage.")
    private static void sendFileToServer(String pathFileToSend){

        return;
    }

    /**
     * Submit the file, found in the path' parameter, to the storage server <Protocol SFTP>
     * @param pathFileToGet Path of the file to get
     */
    @_Todo(level = Level.EVOLUTION, comment = "Pouvoir recevoir les fichiers depuis le serveur de stockage.")
    private static void getFileFromServer(String pathFileToGet){

        return;
    }

    /**
     * Check if the file already exists at the destination
     * @param fileNameWithoutExt file name without its extension
     */
    private static boolean fileAlreadyExists(String fileNameWithoutExt){
        File fileToOverwrite = new File(GlobalVariables.outputZipPath + "\\" + fileNameWithoutExt + ".zip");
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
