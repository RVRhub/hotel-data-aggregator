package com.rvr.hotel.data.aggregator.services.domain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import com.rvr.hotel.data.aggregator.storage.ImagesStorage;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImagesExtractorTest {
    @Mock
    ImagesStorage imagesStorage;

    @Test
    void shouldNotExtractImagesFromCoahObjectWhenNoImages() {
        JSONObject jsonObject = new JSONObject("{\"content\": {\"hotel\": {\"test\": \"10\"}}}");

        ImagesExtractor imagesExtractor = new ImagesExtractor(imagesStorage);
        imagesExtractor.extractImagesFromCoahObject(jsonObject, UUID.randomUUID().toString());

        verify(imagesStorage, never()).save(any(), any());
    }


    @Test
    void shouldExtractImagesFromCoahObject() {
        JSONObject jsonObject = new JSONObject("""
                {
                  "content": {
                    "hotel": {
                      "images": {
                        "image": [
                          {
                            "url": "https://media.licdn.com/dms/image/D4D12AQGgMCavCcCbEg/article-cover_image-shrink_600_2000/0/1711086616356?e=2147483647&v=beta&t=mQHOr0eM90WLdtUXavUS9WvYtWd3JUGUxNS5uLCahwM"
                          }
                        ]
                      }
                    }
                  }
                }

                """);

        String operationId = UUID.randomUUID().toString();
        ImagesExtractor imagesExtractor = new ImagesExtractor(imagesStorage);
        imagesExtractor.extractImagesFromCoahObject(jsonObject, operationId);

        verify(imagesStorage).save("https://media.licdn.com/dms/image/D4D12AQGgMCavCcCbEg/article-cover_image-shrink_600_2000/0/1711086616356?e=2147483647&v=beta&t=mQHOr0eM90WLdtUXavUS9WvYtWd3JUGUxNS5uLCahwM", operationId);
    }
}
