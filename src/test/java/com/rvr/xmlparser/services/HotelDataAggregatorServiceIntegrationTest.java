package com.rvr.xmlparser.services;

import com.rvr.xmlparser.services.domain.ImagesExtractor;
import com.rvr.xmlparser.storage.ImagesStorage;
import com.rvr.xmlparser.storage.JsonFileStorage;
import com.rvr.xmlparser.storage.SourceFileExtractor;
import com.rvr.xmlparser.utils.CleanUpImageStorageUtils;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class HotelDataAggregatorServiceIntegrationTest {

    @Autowired
    SourceFileExtractor sourceFileExtractor;

    @Autowired
    JsonFileStorage jsonFileStorage;

    @Autowired
    ImagesExtractor imagesExtractor;

    @Autowired
    ImagesStorage imagesStorage;

    @Value("classpath:storage/result/images/")
    Resource imagesResourceDir;

    @Test
    void shouldAggregateHotelData() {

        CleanUpImageStorageUtils.withCleanUpImageFolder(imagesResourceDir, () -> {

            HotelDataAggregatorService hotelDataAggregatorService = new HotelDataAggregatorService(sourceFileExtractor, jsonFileStorage, imagesExtractor);

            String operationId = hotelDataAggregatorService.aggregateHotelData();

            assertThat(operationId).isNotNull();
            List<String> images = imagesStorage.getImages(operationId);
            assertThat(images).isNotNull();
            assertThat(images.size()).isEqualTo(1);

            JSONArray jsonArray = jsonFileStorage.getJsonArrayById(operationId);
            assertThat(jsonArray).isNotNull();
            assertThat(jsonArray.length()).isEqualTo(6);
        });
    }
}
