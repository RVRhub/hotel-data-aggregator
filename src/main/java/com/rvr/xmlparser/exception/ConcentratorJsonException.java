package com.rvr.xmlparser.exception;

public class ConcentratorJsonException extends RuntimeException
{
	public ConcentratorJsonException(String message, Exception exception)
	{
		super(message, exception);
	}
}
