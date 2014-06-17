package io.core9.plugin.rest;

import com.wordnik.swagger.config.SwaggerConfig;

public class RestResourceConfigImpl implements RestResourceConfig {


	
	private SwaggerConfig swaggerConfig;
	private String modelPackage;


	@Override
	public SwaggerConfig getSwaggerConfig() {
		return swaggerConfig;
	}

	@Override
	public void setSwaggerConfig(SwaggerConfig swaggerConfig) {
		this.swaggerConfig = swaggerConfig;
	}

	@Override
	public void setModelPackage(String modelPackage) {
		this.modelPackage = modelPackage;
	}
	
	@Override
	public String getModelPackage() {
		return modelPackage;
	}



}
