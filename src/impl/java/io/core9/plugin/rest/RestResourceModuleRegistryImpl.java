package io.core9.plugin.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.sun.jersey.api.uri.UriTemplate;
import com.sun.jersey.api.uri.UriTemplateParser;

@PluginImplementation
public class RestResourceModuleRegistryImpl implements
		RestResourceModuleRegistry {

	private Map<String, RestResource> resources = new HashMap<>();
	private Map<String, List<Map<String, Object>>> indexedResources = new HashMap<>();

	@Override
	public void setResources(Map<String, RestResource> resources) {
		if (resources != null) {
			for (Entry<String, RestResource> resource : resources.entrySet()) {
				this.resources.put(resource.getKey(), resource.getValue());
				indexResource(resource.getKey(), resource.getValue());
			}

		}
	}

	private void indexResource(String key, RestResource restResource) {
		JSONArray resources = (JSONArray) restResource.getApi().get("apis");
		for (Object resource : resources) {
			JSONObject jsonObj = (JSONObject) resource;

			processRequest(jsonObj, restResource);
		}
	}

	@Override
	public void processRequest(JSONObject jsonObj, RestResource restResource) {

		JSONArray operations = (JSONArray) jsonObj.get("operations");

		for (Object operation : operations) {

			Map<String, Object> resourceMap = new HashMap<>();
			
			System.out.println("");
			String path = (String) jsonObj.get("path");
			System.out.println("path : " + path);
			resourceMap.put("path", path);
			
			resourceMap.put("modelPackage", restResource.getModelPackage());

			JSONObject op = (JSONObject) operation;

			String nickname = (String) op.get("nickname");
			System.out.println("nickname : " + nickname);
			resourceMap.put("nickname", nickname);

			String method = (String) op.get("method");
			System.out.println("method : " + method);
			resourceMap.put("method", method);
			
			JSONArray parameters = (JSONArray) op.get("parameters");
			System.out.println("parameters : " + parameters);
			resourceMap.put("parameters", parameters);

			
			UriTemplateParser parser = new UriTemplateParser(path);
			String template = parser.getNormalizedTemplate();
			System.out.println("normalizedTemplate : " + template);
			resourceMap.put("normalizedTemplate", template);

			Map<String, String> map = new HashMap<String, String>();
			UriTemplate uriTemplate = new UriTemplate(template);
			if (uriTemplate.match(path, map)) {
				System.out.println("Matched, " + map);
			} else {
				System.out.println("Not matched, " + map);
			}

			String hash = path.split("/")[1] + method
					+ (path.split("/").length - 1);
			System.out.println("Hash : " + hash);
			
			addToResourceMap(hash, resourceMap);
			
		}

	}
	
	private void addToResourceMap(String hash, Map<String, Object> resourceMap) {
		List<Map<String, Object>> resourceMapList = new ArrayList<>();
		
		List<Map<String, Object>> value = indexedResources.get(hash);
		if (value != null) {
			
			resourceMapList.add(resourceMap);
			resourceMapList.addAll(value);
			
			indexedResources.put(hash, resourceMapList);
			
		} else {
			resourceMapList.add(resourceMap);
			indexedResources.put(hash, resourceMapList);
		}
	}

	@Override
	public List<Map<String, Object>> getResourceMap(String hash){
		return indexedResources.get(hash);
	}

	@Override
	public RestResource getResource(String resourceName) {
		return resources.get(resourceName);
	}

}
