package io.core9.plugin.rest;

import net.minidev.json.JSONObject;

import com.wordnik.swagger.config.SwaggerConfig;

public interface RestResource {




	Object getResourceObject();

	void setResourceObject(SwaggerConfig config, Object resourceObject);

	JSONObject getApi();

	String getModelPackage();

	void setModelPackage(String modelPackage);



}
