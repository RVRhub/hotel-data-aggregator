package com.rvr.hotel.data.aggregator.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.rvr.hotel.data.aggregator.exception.JsonFileStorageException;
import org.json.JSONArray;
import org.json.JSONPointerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class JsonFileStorage
{
	public static final String RESULT_FOLDER = "/result";
	public static final String JSON_EXTENSION = ".json";
	public static final String PATH_DELIMITER = "/";
	private final Resource storageResourceDir;

	public JsonFileStorage(@Value("classpath:storage/") Resource storageResourceDir)
	{
		this.storageResourceDir = storageResourceDir;
	}

	public boolean save(JSONArray singleJsonFile, String operationId) throws IOException
	{
		var singleResult = Paths.get(storageResourceDir.getFile().getAbsolutePath() + RESULT_FOLDER + PATH_DELIMITER +  operationId + JSON_EXTENSION);
		Files.write(singleResult, singleJsonFile.toString(4).getBytes());

		return true;
	}

	public JSONArray getJsonArrayById(String operationId) {
		try {
			var filePath = Paths.get(storageResourceDir.getFile().getAbsolutePath() + RESULT_FOLDER + PATH_DELIMITER + operationId + JSON_EXTENSION);
			String content = Files.readString(filePath);
			return new JSONArray(content);
		}
		catch (IOException e)
		{
			throw new JSONPointerException("Can't read file with id: " + operationId, e);
		}
	}

	public void createResultFolderIfNotExist() {
		try {
			if (!Files.exists(Paths.get(storageResourceDir.getFile().getAbsolutePath() + RESULT_FOLDER))) {
				Files.createDirectories(Paths.get(storageResourceDir.getFile().getAbsolutePath() + RESULT_FOLDER));
			}
		}
		catch (IOException e)
		{
			throw new JsonFileStorageException("Can't create result folder.", e);
		}
	}
}
