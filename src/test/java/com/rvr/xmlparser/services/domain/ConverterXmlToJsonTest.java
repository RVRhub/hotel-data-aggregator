package com.rvr.xmlparser.services.domain;

import com.rvr.xmlparser.exception.XmlToJsonParsingException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ConverterXmlToJsonTest
{
    @Test
	void shouldThrowXmlToJsonParsingExceptionWhenFileIsNotFound() {
		ConverterXmlToJson converterXmlToJson = new ConverterXmlToJson();

		File sourceFile = new File("file.xml");

		XmlToJsonParsingException xmlToJsonParsingException = Assertions.assertThrows(
				XmlToJsonParsingException.class,
				() -> converterXmlToJson.convert(sourceFile)
		);
		assertTrue(xmlToJsonParsingException.getMessage().contains("File not found: file.xml"));
	}

	@Test
	void shouldThrowXmlToJsonParsingExceptionWhenFileHasInvalidContent() throws IOException {
		ConverterXmlToJson converterXmlToJson = new ConverterXmlToJson();

		File tempFile = File.createTempFile("invalidContent", ".xml");
		try (FileWriter fileWriter = new FileWriter(tempFile)) {
			fileWriter.write("<f>wrong content</aaaa>");
		}

		XmlToJsonParsingException exception = Assertions.assertThrows(
				XmlToJsonParsingException.class,
				() -> converterXmlToJson.convert(tempFile)
		);

		assertTrue(exception.getMessage().contains("The problem with file: " + tempFile.getName()));

		tempFile.delete();
	}

	@Test
	void shouldConvertXmlToJson() throws IOException {
		Resource resource = new ClassPathResource("/storage/3956-coah.xml");
		File xmlFile = resource.getFile();

		ConverterXmlToJson converterXmlToJson = new ConverterXmlToJson();
		JSONObject convert = converterXmlToJson.convert(xmlFile);

		assertThat(convert).isNotNull();
		assertThat(convert.get("content")).isNotNull();
	}
}
