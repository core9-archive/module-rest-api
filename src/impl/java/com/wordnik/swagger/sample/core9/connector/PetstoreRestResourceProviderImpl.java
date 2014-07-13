package com.wordnik.swagger.sample.core9.connector;

import io.core9.plugin.rest.RestResource;
import io.core9.plugin.rest.RestResourceConfig;
import io.core9.plugin.rest.RestResourceConfigImpl;
import io.core9.plugin.rest.RestUtils;

import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.sample.resource.PetResource;
import com.wordnik.swagger.sample.resource.PetStoreResource;
import com.wordnik.swagger.sample.resource.UserResource;


@PluginImplementation
public class PetstoreRestResourceProviderImpl implements PetstoreRestResourceProvider {

	private Map<String, RestResource> resourceMap = new HashMap<>();
	
	@Override
	public Map<String, RestResource> getResources() {


		RestResourceConfig restResourceConfig =  new RestResourceConfigImpl();
		
	    SwaggerConfig config = new SwaggerConfig();
	    config.setApiVersion("1.0.1");
	    config.setBasePath("http://localhost:8080/api");

		restResourceConfig.setSwaggerConfig(config);
		restResourceConfig.setModelPackage("com.wordnik.swagger.sample.model");

		
		resourceMap.putAll(RestUtils.addRestResource(restResourceConfig, new PetResource()));
		
		resourceMap.putAll(RestUtils.addRestResource(restResourceConfig, new PetStoreResource()));
		
		resourceMap.putAll(RestUtils.addRestResource(restResourceConfig, new UserResource()));
		
		
		return resourceMap;
	}





}
