package com.rvr.hotel.data.aggregator.services.domain;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.json.JSONObject;

import com.rvr.hotel.data.aggregator.services.SafelyReadJsonObject;

/**
 * This class is responsible for managing unique objects.
 * <p>
 * It keeps track of all unique objects by GiataID and COAH giata_id.
 */
public class UniqueObjectManager implements SafelyReadJsonObject
{
	private final Set<String> uniqGiataObjectInJson = new HashSet<>();
	private final Set<String> uniqCoahObjectInJson = new HashSet<>();

	public boolean isSameObjectNotExist(JSONObject jsonObject)
	{
		return !(getIdFromCoahObject(jsonObject).stream().anyMatch(uniqCoahObjectInJson::contains) ||
			getIdFromGiataObject(jsonObject).stream().anyMatch(uniqGiataObjectInJson::contains));
	}

	public void markObjectAsAdded(JSONObject jsonObject)
	{
		getIdFromCoahObject(jsonObject)
			.ifPresent(uniqCoahObjectInJson::add);

		getIdFromGiataObject(jsonObject)
			.ifPresent(uniqGiataObjectInJson::add);
	}

	private Optional<String> getIdFromGiataObject(JSONObject jsonObject)
	{
		return Optional.ofNullable(safeRead(() -> jsonObject.getJSONObject("result")))
			.map(t -> safeRead(() -> t.getJSONObject("data")))
			.map(t -> safeRead(() -> t.getJSONObject("GeoData")))
			.map(t -> t.getString("GiataID"));
	}

	private Optional<String> getIdFromCoahObject(JSONObject jsonObject)
	{
		return Optional.ofNullable(safeRead(() -> jsonObject.getJSONObject("content")))
			.map(t -> safeRead(() -> t.getJSONObject("hotel")))
			.map(t -> t.getString("giata_id"));
	}
}
