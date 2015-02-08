package com.cylentsystems;

import java.io.*;
import java.security.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * KeyManager
 *
 */
public class KeyManager
{
    private static final Logger logger = LoggerFactory.getLogger(KeyManager.class);
    private KeyStore keyStore;
    private String password;

    KeyManager(String password, File file) throws KeystoreException {
        //open file if it exits or create if it doesn't
        this.keyStore = openOrCreateKeyStore(password,file);
        this.password = password;

    }

    public CryptoPackage aesEncrypt(byte[] input, Key key) throws KeystoreException {
        CryptoPackage pkg;
        try {

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(),"AES");
            cipher.init(Cipher.ENCRYPT_MODE,keySpec);
            pkg = new CryptoPackage(cipher.doFinal(input),cipher.getIV());
        } catch (Exception ex) {
            throw new KeystoreException(ex.getCause());
        }
        logger.debug("AES encryption SUCCESS");
        return pkg;
    }

    public byte[] aesDecrypt(CryptoPackage cipherPkg, Key key) throws KeystoreException {
        byte[] output;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(),"AES");
            cipher.init(Cipher.DECRYPT_MODE,keySpec,new IvParameterSpec(cipherPkg.getCipherIV()));
            output = cipher.doFinal(cipherPkg.getCipherData());
        } catch(Exception ex) {
            throw new KeystoreException(ex.getCause());
        }
        logger.debug("AES decryption SUCCESS");
        return output;
    }

    public Key generateAesKey(String alias) throws KeystoreException
    {
        Key key;
        try {
            SecureRandom random= SecureRandom.getInstance("SHA1PRNG","SUN");
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(128,random);
            key = generator.generateKey();
            this.keyStore.setKeyEntry(alias,key,this.password.toCharArray(),null);
        } catch (Exception ex) {
            throw new KeystoreException(ex.getCause());
        }
        return key;
    }

    public Key getAesKey(String alias) throws KeystoreException {
        Key key;
        try {
            key = this.keyStore.getKey(alias,this.password.toCharArray());
        } catch (Exception ex) {
            throw new KeystoreException(ex.getCause());
        }
        logger.debug("Returning key for alias: "+alias);
        return key;
    }

    private KeyStore openOrCreateKeyStore(String password, File file) throws KeystoreException {
        FileInputStream fis;
        KeyStore keystore;
        try {
            fis = new FileInputStream(file);
            keystore = KeyStore.getInstance("JCEKS");
            keystore.load(fis,password.toCharArray());
        } catch (FileNotFoundException ex) {
            keystore = createKeystore(password, file);
        } catch (Exception ex) {
            throw new KeystoreException(ex.getCause());
        }
        return keystore;
    }
    private KeyStore createKeystore(String password,File file) throws KeystoreException {
        KeyStore keystore = null;
        FileOutputStream fos = null;
        try {
            keystore = KeyStore.getInstance("JCEKS");
            fos = new FileOutputStream(file);
            keystore.load(null,password.toCharArray());
            keystore.store(fos,password.toCharArray());

        } catch (Exception ex) {
            throw new KeystoreException(ex.getCause());
        } finally {
            try {
                fos.close();
            } catch(IOException ex) {
                throw new KeystoreException(ex.getCause());
            }
        }
        return keystore;
    }
}

