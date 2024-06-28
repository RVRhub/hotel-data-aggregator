package com.rvr.hotel.data.aggregator.exception;

public class XmlToJsonParsingException extends RuntimeException
{
	public XmlToJsonParsingException(String message)
	{
		super(message);
	}

	public XmlToJsonParsingException(String message, Exception exception)
	{
		super(message, exception);
	}
}
