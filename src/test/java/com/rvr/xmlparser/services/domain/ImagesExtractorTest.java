package com.rvr.xmlparser.services.domain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.UUID;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rvr.xmlparser.storage.ImagesStorage;

@ExtendWith(MockitoExtension.class)
class ImagesExtractorTest
{
	@Mock
	ImagesStorage imagesStorage;

	@Test
	void shouldNotExtractImagesFromCoahObjectWhenNoImages()
	{
		JSONObject jsonObject = new JSONObject("{\"content\": {\"hotel\": {\"test\": \"10\"}}}");

		ImagesExtractor imagesExtractor = new ImagesExtractor(imagesStorage);
		imagesExtractor.extractImagesFromCoahObject(jsonObject, UUID.randomUUID().toString());

		verify(imagesStorage, never()).save(any(), any());
	}


	@Test
	void shouldExtractImagesFromCoahObject()
	{
		JSONObject jsonObject = new JSONObject("{\n"
			+ "  \"content\": {\n"
			+ "    \"hotel\": {\n"
			+ "      \"images\": {\n"
			+ "        \"image\": [\n"
			+ "          {\n"
			+ "            \"url\": \"https://media.licdn.com/dms/image/D4D12AQGgMCavCcCbEg/article-cover_image-shrink_600_2000/0/1711086616356?e=2147483647&v=beta&t=mQHOr0eM90WLdtUXavUS9WvYtWd3JUGUxNS5uLCahwM\",\n"
			+ "          }\n"
			+ "        ]\n"
			+ "      }\n"
			+ "    }\n"
			+ "  }\n"
			+ "}\n");

		String operationId = UUID.randomUUID().toString();
		ImagesExtractor imagesExtractor = new ImagesExtractor(imagesStorage);
		imagesExtractor.extractImagesFromCoahObject(jsonObject, operationId);

		verify(imagesStorage).save(eq("https://media.licdn.com/dms/image/D4D12AQGgMCavCcCbEg/article-cover_image-shrink_600_2000/0/1711086616356?e=2147483647&v=beta&t=mQHOr0eM90WLdtUXavUS9WvYtWd3JUGUxNS5uLCahwM"), eq(operationId));
	}
}
