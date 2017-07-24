package DropThatFile.engines;

import DropThatFile.controllers.HomeController;
import DropThatFile.engines.APIData.APIModels.APIConfig;
import DropThatFile.models.Group;
import javafx.scene.control.*;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.json.JSONObject;

import java.io.*;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static DropThatFile.GlobalVariables.*;

/**
 * Created by Nicol on 22/03/2017.
 */
public class FilesJobs {

    public JSONObject credentialsFTP;

    public static FilesJobs instance = null;

    public FilesJobs(){
        this.credentialsFTP = APIConfig.Instance().getFTPInformations();
    }

    //region init
    static {
        File f = new File(userRepoMainPath);
        if (!f.exists() || !f.isDirectory()) {
            f.mkdirs();
        }
    }
    //endregion

    public boolean sendFileToServer(File file, boolean isForUser){
        String path = file.getAbsolutePath();
        final String regex = (isForUser) ? "\\\\userfiles.+\\\\" : "\\\\groupfiles.+\\\\";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(path);
        String pathFileFTP = null;
        while (matcher.find()) {
            pathFileFTP = matcher.group(0);
        }
        if(this.sendToFTP(file, pathFileFTP)){
            return true;
        }
        return false;
    }

    private boolean sendToFTP(File file, String path){
        InputStream fileToSend;
        FTPClient ftpClient = null;
        try {
            ftpClient = getFTPConnexion();
            ftpClient.makeDirectory(path);
            fileToSend = new FileInputStream(file);
            ftpClient.storeFile(path + file.getName(), fileToSend);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                ftpClient.logout();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Encrypt and send the archive to the storage server
     * @param filesInArchive List of files to add in the archive
     */
    public boolean sendEncryptedArchive(List<File> filesInArchive, TreeItem<File> selectedFolder) throws ZipException {
        // Zip name
        String fileNameWithoutExt = HomeController.zipName.replaceFirst("[.][^.]+$", "");

        // Zip future location
        ZipFile zipFile = new ZipFile(selectedFolder.getValue().getAbsolutePath() + "\\" + fileNameWithoutExt + ".zip");

        ArrayList<File> filesToAdd = new ArrayList<>();
        filesToAdd.addAll(filesInArchive);

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
            sendFileToServer(new File(selectedFolder.getValue().getAbsolutePath() + "\\" + fileNameWithoutExt + ".zip"), true);
            return true;
        } catch(ZipException ex){
            ex.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @return
     */
    public ThreadGroup downloadFiles(){
        Set<Integer> keysOfGroups = currentUser.getIsMemberOf().keySet();
        Integer[] integerIdGroups = keysOfGroups.toArray(new Integer[keysOfGroups.size()]);
        final ArrayList<Integer> groupsPart1 = new ArrayList<>();
        final ArrayList<Integer> groupsPart2 = new ArrayList<>();
        Arrays.stream(integerIdGroups).forEach(s -> {if(s % 2 == 0){groupsPart1.add(s);}else{groupsPart2.add(s);}});

        ThreadGroup thdGrp = new ThreadGroup("FTP Async Thread Group");

        Thread thread1 = new Thread(thdGrp, () -> FilesJobs.Instance().downloadUserFiles());

        Thread thread2 = new Thread(thdGrp, () -> FilesJobs.Instance().downloadGroupFiles(groupsPart1));

        Thread thread3 = new Thread(thdGrp, () -> FilesJobs.Instance().downloadGroupFiles(groupsPart2));

        thread1.start();
        thread2.start();
        thread3.start();

        return thdGrp;
    }

    /**
     *
     */
    public void downloadUserFiles() {
        FTPClient ftpClient = null;
        String firstCharFName = currentUser.getfName().substring(0,1);
        String lName = currentUser.getlName();

        try {
            ftpClient = getFTPConnexion();

            FTPFile[] files = ftpClient.listFiles("/userfiles/" + (firstCharFName + lName));
            ftpClient.changeWorkingDirectory("/userfiles/" + (firstCharFName + lName));

            for (FTPFile file : files) {
                if (file.isDirectory()) {
                    getFilesOnDirectoryRecursive("/userfiles/" + (firstCharFName + lName) + "/" + file.getName(), file.getName());
                    continue;
                }

                new File(currentUserRepoPath).mkdirs();
                File downloadFile = new File(currentUserRepoPath + file.getName());

                if(downloadFile.exists()) continue;

                try(OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile))) {
                    ftpClient.retrieveFile("/userfiles/" + (firstCharFName + lName) + "\\" + file.getName(), outputStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(ftpClient != null)
                try {
                    ftpClient.logout();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     *
     * @param listOfGroups
     */
    public void downloadGroupFiles(ArrayList<Integer> listOfGroups) {
        FTPClient ftpClient = null;
        HashMap<Integer, Group> userGroups = currentUser.getIsMemberOf();

        try {
            for(Integer idGroup : listOfGroups){
                ftpClient = getFTPConnexion();
                String groupName = userGroups.get(idGroup).getName();

                FTPFile[] files = ftpClient.listFiles("/groupfiles/" + groupName);
                ftpClient.changeWorkingDirectory("/groupfiles/" + groupName);

                for (FTPFile file : files) {
                    if (file.isDirectory()) {
                        getFilesOnDirectoryRecursive("/groupfiles/" + groupName + "/" + file.getName(), file.getName());
                        continue;
                    }

                    new File(groupRepoMainPath + groupName).mkdirs();
                    File downloadFile = new File(groupRepoMainPath + groupName + "\\" + file.getName());
                    if(downloadFile.exists()) continue;

                    try(OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile))){
                        ftpClient.retrieveFile("/groupfiles/" + groupName + "\\" + file.getName(), outputStream);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(ftpClient != null)
                try {
                    ftpClient.logout();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public void getFilesOnDirectoryRecursive(String pathFTP, String pathRepository){
        FTPClient ftpClient = null;
        try {
            ftpClient = getFTPConnexion();

            FTPFile[] files = ftpClient.listFiles(pathFTP);
            ftpClient.changeWorkingDirectory(pathFTP);

            for (FTPFile file : files) {
                if (file.isDirectory()) getFilesOnDirectoryRecursive(pathFTP + "/" + file.getName(), pathRepository + "\\" + file.getName());

                new File(currentUserRepoPath + "\\" + pathRepository).mkdirs();
                File downloadFile = new File(currentUserRepoPath + pathRepository + "\\" +file.getName());

                if(downloadFile.exists()) continue;

                try(OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile))) {
                    ftpClient.retrieveFile(pathFTP + "\\" + file.getName(), outputStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(ftpClient != null)
                try {
                    ftpClient.logout();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     *
     * @return
     * @throws IOException
     */
    public FTPClient getFTPConnexion() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(this.credentialsFTP.getString("host"));
        ftpClient.login(this.credentialsFTP.getString("user"), this.credentialsFTP.getString("password"));
        return ftpClient;
    }

    /**
     *
     * @return
     */
    public static FilesJobs Instance(){
        if(instance == null){
            instance = new FilesJobs();
            return instance;
        } else {
            return instance;
        }
    }
}
