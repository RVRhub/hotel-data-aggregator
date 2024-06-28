package com.rvr.xmlparser.services.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rvr.xmlparser.exception.XmlToJsonParsingException;

/**
 * This class converts xml files to json files from the file storage.
 */
@Service
public class ConverterXmlToJson
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ConverterXmlToJson.class);

	/**
	 * Parse the XML files and convert them to JSON objects.
	 *
	 * @param sourceFile the file to be converted to JSON
	 * @return the JSON object from the XML file
	 */
	public JSONObject convert(File sourceFile)
	{
		LOGGER.debug("Converting file: {}", sourceFile.getName());
		if (!sourceFile.exists()) {
			throw new XmlToJsonParsingException(String.format("File not found: %s", sourceFile.getName()));
		}
		try (var xmlFile = new FileInputStream(sourceFile))
		{
			return getJsonObjectFromInputStream(xmlFile);
		}
		catch (Exception e)
		{
			throw new XmlToJsonParsingException(String.format("The problem with file: %s", sourceFile.getName()), e);
		}
	}

	private JSONObject getJsonObjectFromInputStream(FileInputStream xmlIS) throws IOException
	{
		var xmlString = new String(xmlIS.readAllBytes(), StandardCharsets.UTF_8);
		return XML.toJSONObject(xmlString, true);
	}
}
