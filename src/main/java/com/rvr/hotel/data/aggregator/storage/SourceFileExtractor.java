package com.rvr.hotel.data.aggregator.storage;

import com.rvr.hotel.data.aggregator.exception.HotelDataAggregatorException;
import com.rvr.hotel.data.aggregator.services.domain.ConverterXmlToJson;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Service
public class SourceFileExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SourceFileExtractor.class);
    public static final String XML_EXTENSION = ".xml";
    public static final String JSON_EXTENSION = ".json";

    private final ConverterXmlToJson converterXmlToJson;

    private final Resource sourceStorageResourceDirectory;

    public SourceFileExtractor(
            @Value("classpath:storage/") Resource sourceStorageResourceDirectory,
            ConverterXmlToJson converterXmlToJson
    ) {
        this.converterXmlToJson = converterXmlToJson;
        this.sourceStorageResourceDirectory = sourceStorageResourceDirectory;
    }

    public void extract(Consumer<JSONObject> processor) throws IOException {
        Path dir = Paths.get(sourceStorageResourceDirectory.getURI());
        try (Stream<Path> list = Files.list(dir)) {
            if (Files.notExists(dir) || !Files.isDirectory(dir)) {
                throw new IOException("Empty storage or not a directory.");
            }
            list.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .map(this::getJsonObject)
                    .filter(Objects::nonNull)
                    .forEach(processor);
        }
    }

    private JSONObject getJsonObject(File sourceFile) {
		try {
			String name = sourceFile.getName();
			if (name.endsWith(XML_EXTENSION)) {
				return converterXmlToJson.convert(sourceFile);
			} else if (name.endsWith(JSON_EXTENSION)) {
				return readJSONObject(sourceFile);
			} else {
				LOGGER.warn("Unsupported file format: {}", name);
                return null;
			}
		} catch (Exception e) {
			var message = "Can't read file: " + sourceFile.getName();
			LOGGER.error(message);
			throw new HotelDataAggregatorException(message, e);
		}
    }

    private static JSONObject readJSONObject(File sourceFile) throws IOException {
		return new JSONObject(new String(Files.readAllBytes(Paths.get(sourceFile.getPath()))));
	}
}
