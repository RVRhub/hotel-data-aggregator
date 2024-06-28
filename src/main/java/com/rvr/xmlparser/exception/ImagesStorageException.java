package com.rvr.xmlparser.exception;

public class ImagesStorageException extends RuntimeException
{
    public ImagesStorageException(String message)
    {
        super(message);
    }

    public ImagesStorageException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
