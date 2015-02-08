package com.cylentsystems;

/**
 * *****************************************************************************
 * Copyright (c) 2014
 * All rights reserved.
 * Contributors: rberg
 * Cylent Systems - initial API and implementation
 * *****************************************************************************
 */
public class CryptoPackage {
    private byte[] cipherData;
    private byte[] cipherIV;

    public CryptoPackage(byte[] cipherData, byte[] cipherIV) {
        this.cipherData = cipherData;
        this.cipherIV = cipherIV;
    }

    public byte[] getCipherData() {
        return cipherData;
    }

    public byte[] getCipherIV() {
        return cipherIV;
    }
}
