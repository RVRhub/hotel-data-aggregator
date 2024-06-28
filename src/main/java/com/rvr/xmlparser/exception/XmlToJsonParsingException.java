package com.rvr.xmlparser.exception;

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
