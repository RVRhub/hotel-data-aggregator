package com.rvr.xmlparser.exception;

public class HotelDataAggregatorException extends RuntimeException
{
	public HotelDataAggregatorException(String message)
	{
		super(message);
	}

	public HotelDataAggregatorException(String message, Exception exception)
	{
		super(message, exception);
	}
}
