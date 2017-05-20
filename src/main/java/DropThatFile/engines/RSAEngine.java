package DropThatFile.engines;

import javax.crypto.Cipher;
import java.io.InputStream;
import java.security.*;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by Nicol on 21/03/2017.
 *
 * Classe gérant l'aspect sécurité des mots de passe de la solution.
 */
public class RSAEngine {
    /**
     * Private and public RSA keys' generation.
     * @return Keypair
     * @throws Exception
     */
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();

        return pair;
    }

    /**
     * String message ciphering.
     * @param message
     * @param publicKey
     * @return String
     * @throws Exception
     */
    public static String encrypt(String message, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(message.getBytes(UTF_8));

        return Base64.getEncoder().encodeToString(cipherText);
    }

    /**
     * String message deciphering.
     * @param cipherText
     * @param privateKey
     * @return String
     * @throws Exception
     */
    public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);

        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(decryptCipher.doFinal(bytes), UTF_8);
    }

    /**
     * Sign the message with SHA256 and RSA protocols.
     * @param message
     * @param privateKey
     * @return String
     * @throws Exception
     */
    public static String sign(String message, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(message.getBytes(UTF_8));

        byte[] signature = privateSignature.sign();

        return Base64.getEncoder().encodeToString(signature);
    }

    /**
     * Verify the signature of the message previously signed. Same protocols.
     * @param message
     * @param signature
     * @param publicKey
     * @return boolean
     * @throws Exception
     */
    public static boolean verify(String message, String signature, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(message.getBytes(UTF_8));

        byte[] signatureBytes = Base64.getDecoder().decode(signature);

        return publicSignature.verify(signatureBytes);
    }
}
