package com.rvr.hotel.data.aggregator.exception;

public class ConcentratorJsonException extends RuntimeException
{
	public ConcentratorJsonException(String message, Exception exception)
	{
		super(message, exception);
	}
}
