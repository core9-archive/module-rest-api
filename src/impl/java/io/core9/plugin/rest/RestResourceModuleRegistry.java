package io.core9.plugin.rest;

import io.core9.core.plugin.Core9Plugin;

import java.util.List;
import java.util.Map;

import net.minidev.json.JSONObject;

public interface RestResourceModuleRegistry extends Core9Plugin{


	void setResources(Map<String, RestResource> resources);

	RestResource getResource(String apiPath);



	List<Map<String, Object>> getResourceMap(String hash);

	void processRequest(JSONObject jsonObj, RestResource restResource);



}
