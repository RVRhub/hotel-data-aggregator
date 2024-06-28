package com.rvr.xmlparser.services;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.rvr.xmlparser.exception.HotelDataAggregatorException;
import com.rvr.xmlparser.services.domain.ImagesExtractor;
import com.rvr.xmlparser.services.domain.UniqueObjectManager;
import com.rvr.xmlparser.storage.JsonFileStorage;
import com.rvr.xmlparser.storage.SourceFileExtractor;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rvr.xmlparser.services.domain.ConcentratorJsonObjects;

/**
 * This is main service that responsible for the aggregation of all data.
 * It uses the SourceFileExtractor to extract JSON objects from XML files or JSON files.
 * <p>
 * All extracted JSON objects are merged to a single JSON file. All results are stored via the JsonFileStorage.
 * <p>
 * Additionally, the service extracts images from the COAH objects.
 */
@Service
public class HotelDataAggregatorService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(HotelDataAggregatorService.class);

	private final SourceFileExtractor sourceFileExtractor;

	private final JsonFileStorage jsonFileStorage;

	private final ImagesExtractor imagesExtractor;

	public HotelDataAggregatorService(
			SourceFileExtractor sourceFileExtractor,
			JsonFileStorage jsonFileStorage,
			ImagesExtractor imagesExtractor
	) {
		this.sourceFileExtractor = sourceFileExtractor;
		this.jsonFileStorage = jsonFileStorage;
		this.imagesExtractor = imagesExtractor;
	}

	public String aggregateHotelData()
	{
		String operationId = initializeOperation();
		LOGGER.info("Start aggregation of hotel data. Operation id: {}", operationId);
		try
		{
			ConcentratorJsonObjects concentratorJsonObjects = new ConcentratorJsonObjects(new UniqueObjectManager());

			sourceFileExtractor.extract(
					jsonObject -> {
						concentratorJsonObjects.mergeToSingleObject(jsonObject);
						asyncRunWithTimeout(
								() -> imagesExtractor.extractImagesFromCoahObject(jsonObject, operationId),
								3
						);
					}
			);

			jsonFileStorage.save(concentratorJsonObjects.getSingleJsonFile(), operationId);
			LOGGER.info("Aggregation of hotel data is finished. Result id: {}", operationId);
			return operationId;
		}
		catch (IOException exception)
		{
			var message = String.format("Can't aggregate hotel data. Operation id: %s", operationId);
			LOGGER.error(message, exception);
			throw new HotelDataAggregatorException(message);
		}
	}

	public JSONArray getJsonObject(String id)  {
		return jsonFileStorage.getJsonArrayById(id);
	}

	private String initializeOperation() {
		String operationId = UUID.randomUUID().toString();
		jsonFileStorage.createResultFolderIfNotExist();
		imagesExtractor.createImageFolder(operationId);
		return operationId;
	}

	private void asyncRunWithTimeout(Runnable runnable, int seconds) {
		CompletableFuture<Void> future = CompletableFuture.runAsync(runnable);
		try {
			future.get(seconds, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			future.cancel(true);
			LOGGER.info("Operation timed out and cancelled after {} seconds", seconds);
		} catch (InterruptedException | ExecutionException e) {
			LOGGER.error("Error occurred during operation", e);
		}
	}

}
