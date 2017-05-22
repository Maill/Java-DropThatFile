package DropThatFile.engines;

import DropThatFile.GlobalVariables;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.bc.BcX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.Random;

/**
 * Created by Nicol on 08/05/2017.
 * Gère le coffre-fort de clés.
 */
public class KeyStoreFactory {

    //region Méthode : setKeyPairToKeyStore
    /**
     * Crée un lot de clé privée/publique et les stocke dans un coffre-fort.
     * @param password
     * @throws Exception
     */
    public static void setKeyPairToKeyStore(String password) throws Exception {
        KeyPair pair = RSAEngine.generateKeyPair();
        X509Certificate cert = KeyStoreFactory.generateCertificate(pair.getPublic(), pair.getPrivate(), GlobalVariables.currentUser.getlName() + " " + GlobalVariables.currentUser.getfName());
        Certificate[] certificate = new Certificate[1];
        certificate[0] = cert;
        try {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null, password.toCharArray());
            ks.setKeyEntry("user", pair.getPrivate(), password.toCharArray(), certificate);
            // Store away the keystore.
            FileOutputStream fos = new FileOutputStream("./keystoredtf.jks");
            ks.store(fos, password.toCharArray());
            fos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Méthode : getKeyPairFromKeyStore
    /**
     * Retourne un KeyPair du coffre-fort.
     * @param password
     * @return KeyPair
     * @throws Exception
     */
    public static KeyPair getKeyPairFromKeyStore(String password) throws Exception {
        InputStream ins = new FileInputStream("./keystorename.jks");
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(ins, password.toCharArray());   //Keystore password
        KeyStore.PasswordProtection keyPassword = //Key password
                new KeyStore.PasswordProtection(password.toCharArray());

        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry("user", keyPassword);
        java.security.cert.Certificate cert = keyStore.getCertificate("user");

        PublicKey publicKey = cert.getPublicKey();
        PrivateKey privateKey = privateKeyEntry.getPrivateKey();

        return new KeyPair(publicKey, privateKey);
    }
    //endregion

    //region Méthode : generateCertificate
    /**
     * Génère le certificat de la paire de clé.
     * @param publicKey
     * @param privateKey
     * @param fullName
     * @return X509Certificate
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws NoSuchProviderException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws IOException
     * @throws OperatorCreationException
     */
    public static X509Certificate generateCertificate(PublicKey publicKey, PrivateKey privateKey, String fullName) throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, InvalidKeyException, SignatureException, IOException, OperatorCreationException {
        //Définition de l'identité
        X500Name issuerName = new X500Name("CN="+ fullName + "[DTF], O=DTF, L=Paris, ST=France, C=FR");
        //Identité pour le certificat
        X500Name subjectName = issuerName;
        //Numéro de serie pour le certificat
        BigInteger serial = BigInteger.valueOf(new Random().nextInt());

        //Création du certificat
        X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(issuerName, serial, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 86400000L * 365 * 100), subjectName, publicKey);
        builder.addExtension(Extension.subjectKeyIdentifier, false, createSubjectKeyIdentifier(publicKey));
        builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));

        KeyUsage usage = new KeyUsage(KeyUsage.keyCertSign | KeyUsage.digitalSignature | KeyUsage.keyEncipherment | KeyUsage.dataEncipherment | KeyUsage.cRLSign);
        builder.addExtension(Extension.keyUsage, false, usage);

        ASN1EncodableVector purposes = new ASN1EncodableVector();
        purposes.add(KeyPurposeId.id_kp_serverAuth);
        purposes.add(KeyPurposeId.id_kp_clientAuth);
        purposes.add(KeyPurposeId.anyExtendedKeyUsage);
        builder.addExtension(Extension.extendedKeyUsage, false, new DERSequence(purposes));

        X509Certificate cert = signCertificate(builder, privateKey);
        cert.checkValidity(new Date());
        cert.verify(publicKey);

        return cert;

    }
    //endregion

    //region Méthode privée : createSubjectKeyIdentifier
    /**
     * [Méthode privée] Création du certificat de clé publique.
     * @param publicKey
     * @return SubjectKeyIdentifier
     * @throws Exception
     */
    private static SubjectKeyIdentifier createSubjectKeyIdentifier(PublicKey publicKey) throws IOException {
        ASN1InputStream is = null;
        try {
            is = new ASN1InputStream(new ByteArrayInputStream(publicKey.getEncoded()));
            ASN1Sequence seq = (ASN1Sequence) is.readObject();
            SubjectPublicKeyInfo info = new SubjectPublicKeyInfo(seq);
            return new BcX509ExtensionUtils().createSubjectKeyIdentifier(info);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }
        return null;
    }
    //endregion

    //region Méthode privée : signCertificate
    /**
     * [Méthode privée] Création et signature du certificat de la clé privée.
     * @param builder
     * @param privateKey
     * @return X509Certificate
     * @throws Exception
     */
    private static X509Certificate signCertificate(X509v3CertificateBuilder builder, PrivateKey privateKey) throws OperatorCreationException, CertificateException {
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA").setProvider(BouncyCastleProvider.PROVIDER_NAME).build(privateKey);
        return new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getCertificate(builder.build(signer));
    }
    //endregion
}
