package com.rvr.hotel.data.aggregator.controllers;

import com.rvr.hotel.data.aggregator.controllers.dto.ResultOperation;
import com.rvr.hotel.data.aggregator.services.HotelDataAggregatorService;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HotelAggregatorController
{
	private final HotelDataAggregatorService hotelDataAggregatorService;

	public HotelAggregatorController(HotelDataAggregatorService hotelDataAggregatorService)
	{
		this.hotelDataAggregatorService = hotelDataAggregatorService;
	}

	@PostMapping("/aggregate")
	public ResponseEntity<ResultOperation> aggregate()
	{
		String resultId = hotelDataAggregatorService.aggregateHotelData();
		return new ResponseEntity<>(new ResultOperation(resultId), HttpStatus.OK);
	}

	@GetMapping("/json/{id}")
	public ResponseEntity<String> getJsonObject(@PathVariable String id)
	{
		JSONArray jsonArray = hotelDataAggregatorService.getJsonObject(id);
		return new ResponseEntity<>(jsonArray.toString(), HttpStatus.OK);
	}
}
