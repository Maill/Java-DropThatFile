package DropThatFile.engines;

import DropThatFile.GlobalVariables;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by Nicol on 21/03/2017.
 *
 * Classe gérant l'aspect sécurité des mots de passe de la solution.
 */
public class RSAEngine {

    //region Attributs

    //region Instance singleton
    private RSAEngine instance = null;

    public synchronized RSAEngine Instance(){
        if(this.instance == null)
            this.instance = new RSAEngine();
        else
            return this.instance;
        return this.instance;
    }
    //endregion

    //region User KeyPair
    private KeyPair userKeyPair;

    public KeyPair getKeyPair(){ return this.userKeyPair; }
    //endregion

    //endregion

    //region Constructeur privé
    private RSAEngine(){ this.userKeyPair = GlobalVariables.currentUser.getPassword(); }
    //endregion

    //region Méthodes

    //region Méthode statique : generateKeyPair
    /**
     * Génération d'une paire clé publique/privée.
     * @return Keypair
     * @throws Exception
     */
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();

        return pair;
    }
    //endregion

    //region Méthode : encrypt
    /**
     * Chiffrement du message.
     * @param message
     * @return String
     * @throws Exception
     */
    public String encrypt(String message) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, this.userKeyPair.getPublic());

        byte[] cipherText = encryptCipher.doFinal(message.getBytes(UTF_8));

        return Base64.getEncoder().encodeToString(cipherText);
    }
    //endregion

    //region Méthode : decrypt
    /**
     * Déchiffrement du message.
     * @param cipherText
     * @return String
     * @throws Exception
     */
    public String decrypt(String cipherText) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);

        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, this.userKeyPair.getPrivate());

        return new String(decryptCipher.doFinal(bytes), UTF_8);
    }
    //endregion

    //region Méthode : sign
    /**
     * Signature du message avec les protocole RSA et SHA256.
     * @param message
     * @return String
     * @throws Exception
     */
    public String sign(String message) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(this.userKeyPair.getPrivate());
        privateSignature.update(message.getBytes(UTF_8));

        byte[] signature = privateSignature.sign();

        return Base64.getEncoder().encodeToString(signature);
    }
    //endregion

    //region Méthode : verify
    /**
     * Vérifie l'authentificité du message grâce à la signature.
     * @param message
     * @param signature
     * @param publicKey
     * @return boolean
     * @throws Exception
     */
    public boolean verify(String message, String signature, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(message.getBytes(UTF_8));

        byte[] signatureBytes = Base64.getDecoder().decode(signature);

        return publicSignature.verify(signatureBytes);
    }

    public boolean verify(String message, String signature) throws Exception {
        return this.verify(message, signature, this.userKeyPair.getPublic());
    }
    //endregion

    //endregion
}