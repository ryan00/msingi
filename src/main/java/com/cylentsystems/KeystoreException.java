package com.cylentsystems;

/**
 * *****************************************************************************
 * Copyright (c) 2014
 * All rights reserved.
 * Contributors: rberg
 * Cylent Systems - initial API and implementation
 * *****************************************************************************
 */
public class KeystoreException extends Exception {
    public KeystoreException(Throwable cause) {
        initCause(cause);
    }
}
