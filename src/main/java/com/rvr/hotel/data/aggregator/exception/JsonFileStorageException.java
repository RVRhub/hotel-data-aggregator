package com.rvr.hotel.data.aggregator.exception;

public class JsonFileStorageException extends RuntimeException {

    public JsonFileStorageException(String message) {
        super(message);
    }

    public JsonFileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
