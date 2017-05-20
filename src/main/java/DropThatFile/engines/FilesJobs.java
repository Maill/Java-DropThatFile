package DropThatFile.engines;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import java.io.File;

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
    public static void encryptFile(DropThatFile.models.File File, File path) throws ZipException {
        File fileToAdd = null;
        try {
            net.lingala.zip4j.core.ZipFile zipFile = new ZipFile(tmpFilePath + "\\" + File.getName() + ".zip");

            fileToAdd = new File(path.getAbsolutePath() + "\\" + File.getName());

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

            parameters.setPassword(File.getPassword());

            zipFile.addFile(fileToAdd, parameters);

            sendFileToServer(zipFile.getFile().getPath());
        } catch (ZipException e) {
            throw new ZipException();
        }
    }

    //SFTP
    private static void sendFileToServer(String pathFileToSend){

    }
}
