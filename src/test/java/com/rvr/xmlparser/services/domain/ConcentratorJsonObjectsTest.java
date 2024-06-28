package com.rvr.xmlparser.services.domain;

import com.rvr.xmlparser.exception.ConcentratorJsonException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ConcentratorJsonObjectsTest
{

	@Autowired
	UniqueObjectManager uniqueObjectManager;

	@Test
	void shouldThrowConcentratorJsonExceptionWhenObjectCanNotBeAdd()
	{
		ConcentratorJsonObjects concentratorJsonObjects = new ConcentratorJsonObjects(uniqueObjectManager);

		ConcentratorJsonException concentratorJsonException = Assertions.assertThrows(
					ConcentratorJsonException.class,
				() -> concentratorJsonObjects.mergeToSingleObject(null)
		);
		assertTrue(concentratorJsonException.getMessage().contains("Can't merge JSON object to single file."));
	}

	@Test
	void shouldMergeJsonObjectToSingleFile()
	{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("content", "test");

		ConcentratorJsonObjects concentratorJsonObjects = new ConcentratorJsonObjects(new UniqueObjectManager());

		boolean result = concentratorJsonObjects.mergeToSingleObject(jsonObject);
		assertTrue(result);

		JSONArray singleJsonFile = concentratorJsonObjects.getSingleJsonFile();
		assertEquals(1, singleJsonFile.length());
		JSONObject firstObject = (JSONObject) singleJsonFile.get(0);
		assertEquals("test", firstObject.get("content"));
	}

	@Test
	void shouldMergeJsonObjectAndCheckDuplicate()
	{
		String giataId = "123";

		JSONObject jsonObjectOne = new JSONObject();
		jsonObjectOne.put("result", new JSONObject().put("data", new JSONObject().put("GeoData", new JSONObject().put("GiataID", giataId))));

		JSONObject jsonObjectTwo = new JSONObject();
		jsonObjectTwo.put("result", new JSONObject().put("data", new JSONObject().put("GeoData", new JSONObject().put("GiataID", giataId))));

		ConcentratorJsonObjects concentratorJsonObjects = new ConcentratorJsonObjects(new UniqueObjectManager());

		boolean result = concentratorJsonObjects.mergeToSingleObject(jsonObjectOne);
		assertTrue(result);

		result = concentratorJsonObjects.mergeToSingleObject(jsonObjectTwo);
		assertTrue(result);

		JSONArray singleJsonFile = concentratorJsonObjects.getSingleJsonFile();
		assertEquals(1, singleJsonFile.length());
		JSONObject firstObject = (JSONObject) singleJsonFile.get(0);
		assertEquals("123", Optional.ofNullable(firstObject.getJSONObject("result"))
				.map(t -> t.getJSONObject("data"))
				.map(t -> t.getJSONObject("GeoData"))
				.map(t -> t.getString("GiataID"))
				.orElseThrow());
	}
}
