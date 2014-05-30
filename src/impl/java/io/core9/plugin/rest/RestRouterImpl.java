package io.core9.plugin.rest;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import io.core9.plugin.petstore.api.RestResource;
import io.core9.plugin.server.request.Request;


@PluginImplementation
public class RestRouterImpl implements RestRouter {
	
	@InjectPlugin
	private RestResourceModuleRegistry restResourceModuleRegistry;

	@Override
	public void getResponse(String basePath, Request request) {

		//{controller=pet, id=null, type=findByTags, tags=test}
		
		String apiPath = "/" + (String) request.getParams().get("api");
		
		RestResource apiResource = restResourceModuleRegistry.getResource(apiPath);
		
		JSONObject apiJson = apiResource.getApi();
		
		JSONArray apis = (JSONArray) apiJson.get("apis");
		
		for(Object api : apis){
			
			JSONObject jsonObj = (JSONObject)api;

			System.out.println(((JSONObject)((JSONArray)jsonObj.get("operations")).get(0)).get("method"));
			System.out.println(((JSONObject)((JSONArray)jsonObj.get("operations")).get(0)).get("nickname"));
			System.out.println(jsonObj.get("path"));

		}
		
		Object apiObject = apiResource.getResourceObject();
		
		System.out.println(request);
		
	}
	



}
