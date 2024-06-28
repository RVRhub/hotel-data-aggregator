package com.rvr.hotel.data.aggregator.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import com.rvr.hotel.data.aggregator.exception.ImagesStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class ImagesStorage
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ImagesStorage.class);
	public static final String IMAGES_FOLDER = "/result/images";
	public static final String PATH_DELIMITER = "/";
	private final Resource imagesResourceDir;

	public ImagesStorage(@Value("classpath:storage/") Resource imagesResourceDir)
	{
		this.imagesResourceDir = imagesResourceDir;
	}

	public void save(String url, String operationId)
	{
		try (InputStream in = new URL(url).openStream())
		{
			if (in.available() == 0)
			{
				LOGGER.warn("Image with invalid url: {}", url);
				return;
			}
			LOGGER.info("Saving image with url: {}", url);
			var imageFileName = url.substring(url.lastIndexOf('/') + 1);

			Files.copy(in, Paths.get(imagesResourceDir.getFile().getAbsolutePath() + IMAGES_FOLDER + PATH_DELIMITER + operationId + PATH_DELIMITER + imageFileName), StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException e)
		{
			LOGGER.error("Can't save image with url: {}", url, e);
		}
	}

	public List<String> getImages(String operationId)
	{
		Path dirPath = getPath(operationId);
		try (var files = Files.list(dirPath))
		{
			return files.map(Path::getFileName).map(Path::toString).toList();
		}
		catch (IOException e)
		{
			LOGGER.error("Can't get images for operationId: {}", operationId, e);
			return List.of();
		}
	}

	public void createImageOperationFolder(String operationId) {
		try {
			if (!Files.exists(Paths.get(imagesResourceDir.getFile().getAbsolutePath() + IMAGES_FOLDER ))) {
				LOGGER.info("Creating image folder full path: {}", imagesResourceDir.getFile().getAbsolutePath() + IMAGES_FOLDER);

				Files.createDirectories(Paths.get(imagesResourceDir.getFile().getAbsolutePath() + IMAGES_FOLDER));
			}

			Files.createDirectories(Paths.get(imagesResourceDir.getFile().getAbsolutePath() + IMAGES_FOLDER + PATH_DELIMITER + operationId + PATH_DELIMITER));
		} catch (IOException e) {
			LOGGER.error("Can't create image folder for operationId: {}", operationId, e);
		}
	}

	private Path getPath(String operationId)  {
        try {
            return Paths.get(imagesResourceDir.getFile().getAbsolutePath() + IMAGES_FOLDER  + PATH_DELIMITER + operationId + PATH_DELIMITER);
        } catch (IOException e) {
			throw new ImagesStorageException("Can't get path for operationId: " + operationId, e);
        }
	}
}
