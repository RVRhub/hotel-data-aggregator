package com.rvr.hotel.data.aggregator.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rvr.hotel.data.aggregator.controllers.dto.ResultOperation;
import com.rvr.hotel.data.aggregator.services.HotelDataAggregatorService;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class HotelAggregatorControllerTest
{
	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
    HotelDataAggregatorService hotelDataAggregatorService;

	@Test
	void shouldReturnAllXmlDocumentsConvertedToJson()
	{
		String resultId = UUID.randomUUID().toString();
		when(hotelDataAggregatorService.aggregateHotelData()).thenReturn(resultId);
		ResultOperation result = restTemplate.postForObject("/api/aggregate", new HttpEntity<>(null), ResultOperation.class);

		assertThat(result).isNotNull();
		assertThat(result.resultId()).isEqualTo(resultId);
		verify(hotelDataAggregatorService).aggregateHotelData();
	}

	@Test
	void shouldReturnJsonObjectById()
	{
		String jsonId = "123";
		JSONArray jsonObject = new JSONArray()
			.put("abc")
			.put(2);
		when(hotelDataAggregatorService.getJsonObject(jsonId)).thenReturn(jsonObject);

		ResponseEntity<String> response = restTemplate.getForEntity("/api/json/{id}", String.class, jsonId);

		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(response.getBody()).isEqualTo("[\"abc\",2]");
	}

}
