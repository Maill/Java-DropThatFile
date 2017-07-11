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
    private static RSAEngine instance = null;

    public static synchronized RSAEngine Instance(){
        if(instance == null)
            instance = new RSAEngine();
        else
            return instance;
        return instance;
    }
    //endregion

    //endregion

    //region Constructeur privé
    private RSAEngine(){ }
    //endregion

    //region Méthodes

    //region Méthode statique : generateKeyPair
    /**
     * Génération d'une paire clé publique/privée.
     * @return Keypair
     * @throws Exception Java Exception
     */
    static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());

        return generator.generateKeyPair();
    }
    //endregion

    //region Méthodes : [Privée] encrypt & encrypt & getEncryptedPasswdForAPI & setEncryptedPasswdForLocalUsage
    /**
     * Chiffrement du message.
     * @param message Message à chiffrer.
     * @param publicKey Clé publique servant au cryptage
     * @return String
     * @throws Exception Java Exception
     */
    public String encrypt(String message, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(message.getBytes(UTF_8));

        return Base64.getEncoder().encodeToString(cipherText);
    }

    /**
     * Chiffrement du message avec la clé publique de l'utilisateur courrant.
     * @param message Message à chiffrer.
     * @return String
     * @throws Exception Java Exception
     */
    public String encrypt(String message) throws Exception {
        return this.encrypt(message, GlobalVariables.currentUser.getUserKeys().getPublic());
    }

    /**
     * Chiffrement du mot de passe de l'utilisateur avec la clé publique du serveur.
     * Renvoie le mot de passe encrypté au bon format pour l'API.
     * @param nonEncryptedPassword Mot de passe utilisateur en clair.
     * @return String
     * @throws Exception Java Exception
     */
    public String getEncryptedPasswdForAPI(String nonEncryptedPassword) throws Exception{
        return this.encrypt(nonEncryptedPassword, GlobalVariables.public_key_server);
    }

    /**
     * Chiffrement du mot de passe de l'utilisateur avec la clé publique de l'utilisateur courrant.
     * Permet de se servir du mot de passe de l'utilisateur avec le bon chiffrement.
     * @param nonEncryptedPassword Mot de passe utilisateur en clair.
     * @throws Exception Java Exception
     */
    public void setEncryptedPasswdForLocalUsage(String nonEncryptedPassword) throws Exception{
        GlobalVariables.currentUser.setPassword(this.encrypt(nonEncryptedPassword));
    }
    //endregion

    //region Méthode : decrypt
    /**
     * Déchiffrement du message.
     * @param cipherText Message chiffré.
     * @return String
     * @throws Exception Java Exception
     */
    public String decrypt(String cipherText) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);

        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, GlobalVariables.currentUser.getUserKeys().getPrivate());

        return new String(decryptCipher.doFinal(bytes), UTF_8);
    }
    //endregion

    //region Méthode : sign
    /**
     * Signature du message avec les protocole RSA et SHA256.
     * @param message Message chiffré.
     * @return String
     * @throws Exception Java Exception
     */
    public String sign(String message) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(GlobalVariables.currentUser.getUserKeys().getPrivate());
        privateSignature.update(message.getBytes(UTF_8));

        byte[] signature = privateSignature.sign();

        return Base64.getEncoder().encodeToString(signature);
    }
    //endregion

    //region Méthode : verify
    /**
     * Vérifie l'authentificité du message grâce à la signature.
     * @param message Message chiffré.
     * @param signature Signature du message chiffré.
     * @param publicKey Clé publique servant à la verification
     * @return boolean
     * @throws Exception Java Exception
     */
    public boolean verify(String message, String signature, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(message.getBytes(UTF_8));

        byte[] signatureBytes = Base64.getDecoder().decode(signature);

        return publicSignature.verify(signatureBytes);
    }

    /**
     * Vérifie l'authentificité du message grâce à la signature.
     * En utilisant la clé publique de l'utilisateur.
     * @param message Message chiffré.
     * @param signature Signature du message chiffré.
     * @return boolean
     * @throws Exception Java Exception
     */
    public boolean verify(String message, String signature) throws Exception {
        return this.verify(message, signature, GlobalVariables.currentUser.getUserKeys().getPublic());
    }
    //endregion

    //endregion
}