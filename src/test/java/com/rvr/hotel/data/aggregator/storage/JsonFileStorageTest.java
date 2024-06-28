package com.rvr.hotel.data.aggregator.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
class JsonFileStorageTest
{

	@Test
	void shouldReturnJsonArrayFromResultStorage()
	{
		Resource mockResource = new ClassPathResource("/storage/");

		JsonFileStorage jsonFileStorage = new JsonFileStorage(mockResource);
		JSONArray jsonArray = jsonFileStorage.getJsonArrayById("3956-coah");

		assertNotNull(jsonArray);
		assertEquals(jsonArray.get(0), "abc");
		assertEquals(jsonArray.get(1), 2);
	}

	@Test
	void shouldSaveJsonArrayToResultStorage() throws IOException
	{
		JSONArray jsonArray = mock(JSONArray.class);
		when(jsonArray.toString(4)).thenReturn("[\"abc\",2]");
		Resource mockResource = new ClassPathResource("/storage/");

		JsonFileStorage jsonFileStorage = new JsonFileStorage(mockResource);
		String operationId = "3956-coah";
		Boolean result = jsonFileStorage.save(jsonArray, operationId);

		assertTrue(result);

		jsonArray = jsonFileStorage.getJsonArrayById(operationId);
		assertNotNull(jsonArray);
		assertEquals(jsonArray.get(0), "abc");
		assertEquals(jsonArray.get(1), 2);
	}
}
