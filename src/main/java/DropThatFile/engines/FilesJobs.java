package DropThatFile.engines;

import DropThatFile.GlobalVariables;
import DropThatFile.controllers.HomeController;
import DropThatFile.models.*;
import javafx.scene.control.*;
import javafx.scene.control.ButtonType;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.io.File;
import java.util.*;

/**
 * Created by Nicol on 22/03/2017.
 */
public class FilesJobs {

    public static FilesJobs instance = null;

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
    public void sendFiles(File file) {

    }

    /**
     * Encrypt and send the archive to the storage server
     * @param fileModel DropThatFile.models.File file model describing the actual file
     * @param filesInArchive List of files to add in the archive
     */
    public void sendEncryptedArchive(DropThatFile.models.File fileModel, List<File> filesInArchive) throws ZipException {
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

    public ThreadGroup retrieveFilesFromServer(){
        Set<Integer> keysOfGroups = GlobalVariables.currentUser.getIsMemberOf().keySet();
        Integer[] integerIdGroups = keysOfGroups.toArray(new Integer[keysOfGroups.size()]);
        final ArrayList<Integer> groupsPart1 = new ArrayList<>();
        final ArrayList<Integer> groupsPart2 = new ArrayList<>();
        Arrays.stream(integerIdGroups).forEach(s -> {if(s % 2 == 0){groupsPart1.add(s);}else{groupsPart2.add(s);}});

        ThreadGroup thdGrp = new ThreadGroup("FTP Async Thread Group");

        Thread thread1 = new Thread(thdGrp, () -> FilesJobs.Instance().retrieveUserFilesFromServer());

        Thread thread2 = new Thread(thdGrp, () -> FilesJobs.Instance().retrieveGroupFilesFromServer(groupsPart1));

        Thread thread3 = new Thread(thdGrp, () -> FilesJobs.Instance().retrieveGroupFilesFromServer(groupsPart2));

        thread1.start();
        thread2.start();
        thread3.start();

        return thdGrp;
    }


    public void retrieveUserFilesFromServer(){
        FTPClient ftpClient = null;
        try {
            ftpClient = getFTPConnexion();

            FTPFile[] files = ftpClient.listFiles("/userfiles/" + GlobalVariables.currentUser.getId());
            ftpClient.changeWorkingDirectory("/userfiles/" + GlobalVariables.currentUser.getId());

            for (FTPFile file : files) {
                if (file.isDirectory()) {
                    continue;
                }
                new File(GlobalVariables.outputZipPath + "user_" + GlobalVariables.currentUser.getId()).mkdirs();
                File downloadFile = new File(GlobalVariables.outputZipPath + "user_" + GlobalVariables.currentUser.getId() + "\\" + file.getName());
                if(downloadFile.exists()){
                    continue;
                }
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
                boolean success = ftpClient.retrieveFile("/userfiles/" + GlobalVariables.currentUser.getId() + "\\" + file.getName(), outputStream);
                outputStream.close();
            }
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void retrieveGroupFilesFromServer(ArrayList<Integer> listOfGroups){
        FTPClient ftpClient = null;
        try {
            for(Integer idGroup : listOfGroups){
                ftpClient = getFTPConnexion();

                FTPFile[] files = ftpClient.listFiles("/groupfiles/" + idGroup);
                ftpClient.changeWorkingDirectory("/groupfiles/" + idGroup);

                for (FTPFile file : files) {
                    if (file.isDirectory()) {
                        continue;
                    }
                    new File(GlobalVariables.outputZipPath + "group_" + idGroup).mkdirs();
                    File downloadFile = new File(GlobalVariables.outputZipPath + "group_" + idGroup + "\\" + file.getName());
                    if(downloadFile.exists()){
                        continue;
                    }
                    OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
                    boolean success = ftpClient.retrieveFile("/groupfiles/" + idGroup + "\\" + file.getName(), outputStream);
                    outputStream.close();
                }
                ftpClient.logout();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FTPClient getFTPConnexion() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect("localhost");
        ftpClient.login("dtfftpaccount", "password");
        return ftpClient;
    }


    /**
     * Check if the file already exists at the destination
     * @param fileNameWithoutExt file name without its extension
     */
    private boolean fileAlreadyExists(String fileNameWithoutExt){
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

    public static FilesJobs Instance(){
        if(instance == null){
            instance = new FilesJobs();
            return instance;
        } else {
            return instance;
        }
    }
}
