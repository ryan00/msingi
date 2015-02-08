package com.cylentsystems;


import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.any;

/**
 * Unit test for simple KeyManager.
 */
public class KeystoreTest

{
    @Test
    public void testKeystoreCreation() throws KeystoreException, IOException {
       KeyManager manager = new KeyManager("test",new File("testKeyFile"));
       assertThat(manager,is(any(KeyManager.class)));
       Files.delete(new File("testKeyFile").toPath());
    }

    @Test
    public void testKeystoreGenerateKey() throws KeystoreException, IOException {
        KeyManager manager = new KeyManager("test",new File("testKeyFile"));
        assertThat(manager,is(any(KeyManager.class)));
        Key newKey = manager.generateAesKey("testAlias");
        Key retrieveKey = manager.getAesKey("testAlias");
        assertThat(newKey, is(equalTo(retrieveKey)));
        Files.delete(new File("testKeyFile").toPath());
    }
    @Test
    public void testAESEncryption() throws KeystoreException, IOException {
        KeyManager manager = new KeyManager("test",new File("testKeyFile"));
        assertThat(manager,is(any(KeyManager.class)));
        Key newKey = manager.generateAesKey("testAlias");
        String message = "This is a test message";
        CryptoPackage pkg = manager.aesEncrypt(message.getBytes(),newKey);
        assertThat(pkg.getCipherData(),not(equalTo(message.getBytes())));
        byte[] decryptedMessage = manager.aesDecrypt(pkg,newKey);
        assertThat(decryptedMessage,is(equalTo(message.getBytes())));
        Files.delete(new File("testKeyFile").toPath());
    }

}
