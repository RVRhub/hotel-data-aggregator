package com.rvr.xmlparser.services;

import java.util.function.Supplier;
import org.json.JSONObject;

public interface SafelyReadJsonObject
{
	default JSONObject safeRead(Supplier<JSONObject> content)
	{
		try
		{
			return content.get();
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
