package io.core9.plugin.rest;

import net.minidev.json.JSONObject;

import com.wordnik.swagger.config.SwaggerConfig;

public class RestResourceImpl implements RestResource {

	private JSONObject api;

	private Object resourceObject;
	
	private String modelPackage;

	@Override
	public Object getResourceObject() {
		return resourceObject;
	}

	@Override
	public void setResourceObject(SwaggerConfig config, Object resourceObject) {
		this.resourceObject = resourceObject;
		setApi(RestUtils.getApiFromResource(config.getApiVersion(),
				config.getBasePath(), config.getApiPath(),
				resourceObject.getClass()));
	}

	@Override
	public JSONObject getApi() {
		return api;
	}

	private void setApi(JSONObject api) {
		this.api = api;
	}

	@Override
	public String getModelPackage() {
		return modelPackage;
	}

	@Override
	public void setModelPackage(String modelPackage) {
		this.modelPackage = modelPackage;
	}

}
