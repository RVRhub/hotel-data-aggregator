package com.rvr.hotel.data.aggregator.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.rvr.hotel.data.aggregator.exception.HotelDataAggregatorException;
import com.rvr.hotel.data.aggregator.services.domain.ImagesExtractor;
import com.rvr.hotel.data.aggregator.storage.JsonFileStorage;
import com.rvr.hotel.data.aggregator.storage.SourceFileExtractor;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HotelDataAggregatorServiceTest
{
	@Mock
    SourceFileExtractor sourceFileExtractor;

	@Mock
    JsonFileStorage jsonFileStorage;

	@Mock
    ImagesExtractor imagesExtractor;

	@Test
	void shouldReturnJsonArrayFromResultStorage()
	{
		String jsonFileId = "123";
		JSONArray obj1 = new JSONArray()
			.put("abc")
			.put(2);
		when(jsonFileStorage.getJsonArrayById(jsonFileId)).thenReturn(obj1);

		HotelDataAggregatorService hotelDataAggregatorService = new HotelDataAggregatorService(sourceFileExtractor, jsonFileStorage, null);
		JSONArray allJsonObject = hotelDataAggregatorService.getJsonObject(jsonFileId);

		assertThat(allJsonObject).isEqualTo(obj1);
	}

	@Test
	void shouldAggregateHotelData() throws IOException
	{
		HotelDataAggregatorService hotelDataAggregatorService = new HotelDataAggregatorService(sourceFileExtractor, jsonFileStorage, imagesExtractor);

		hotelDataAggregatorService.aggregateHotelData();

		verify(sourceFileExtractor).extract(any());
		verify(jsonFileStorage).save(any(), any());
	}

	@Test
	void shouldThrowHotelDataAggregatorExceptionWhenExtractingSourceFile() throws IOException
	{
		HotelDataAggregatorService hotelDataAggregatorService = new HotelDataAggregatorService(sourceFileExtractor, jsonFileStorage, imagesExtractor);
		doThrow(new IOException()).when(sourceFileExtractor).extract(any());

		assertThatThrownBy(hotelDataAggregatorService::aggregateHotelData)
			.isInstanceOf(HotelDataAggregatorException.class).hasMessageContaining("Can't aggregate hotel data. Operation id");
	}
}
