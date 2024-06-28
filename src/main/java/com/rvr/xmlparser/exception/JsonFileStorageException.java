package com.rvr.xmlparser.exception;

public class JsonFileStorageException extends RuntimeException {

    public JsonFileStorageException(String message) {
        super(message);
    }

    public JsonFileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
