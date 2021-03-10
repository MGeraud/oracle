package com.geraud.data;

import java.util.NoSuchElementException;

public class ProductManagerException extends Exception{
    public ProductManagerException() {
    }

    public ProductManagerException(String message) {
        super(message);
    }

    public ProductManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
