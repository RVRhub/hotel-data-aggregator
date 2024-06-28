package com.rvr.hotel.data.aggregator.storage;


import com.rvr.hotel.data.aggregator.services.domain.ConverterXmlToJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SourceFileExtractorTest {

    @Mock
    Resource imagesResourceDir;

    @Test
    void shouldNotThrowExceptionWhenWrongFile() throws IOException {
        URI uri = new File("src/test/resources/storage/").toURI();
        when(imagesResourceDir.getURI()).thenReturn(uri);

        ConverterXmlToJson converterXmlToJson = new ConverterXmlToJson();
        SourceFileExtractor sourceFileExtractor = new SourceFileExtractor(imagesResourceDir, converterXmlToJson);

        int[] count = {0};
        sourceFileExtractor.extract(jsonObject -> {
            count[0]++;
        });

        assertThat(count[0]).isEqualTo(7);
    }
}