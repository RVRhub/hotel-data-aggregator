package com.rvr.xmlparser.services.domain;

import com.rvr.xmlparser.exception.ConcentratorJsonException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class collect all json objects to one single json array.
 * <p>
 * The result is stored in the singleJsonFile field.
 */
public class ConcentratorJsonObjects
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ConcentratorJsonObjects.class);

	private final UniqueObjectManager uniqueObjectManager;

	private final JSONArray singleJsonFile;

	public ConcentratorJsonObjects(UniqueObjectManager uniqueObjectManager)
	{
		this.uniqueObjectManager = uniqueObjectManager;
		singleJsonFile = new JSONArray();
	}

	public boolean mergeToSingleObject(JSONObject jsonObject) {
		try {
			if (uniqueObjectManager.isSameObjectNotExist(jsonObject)) {
				singleJsonFile.put(jsonObject);
			}

			uniqueObjectManager.markObjectAsAdded(jsonObject);

			return true;
		} catch (Exception e) {
			var message = "Can't merge JSON object to single file.";
			LOGGER.error(message);
			throw new ConcentratorJsonException(message, e);
		}
	}

	public JSONArray getSingleJsonFile() {
		return singleJsonFile;
	}
}
