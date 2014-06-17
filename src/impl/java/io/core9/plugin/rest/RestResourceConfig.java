package io.core9.plugin.rest;

import com.wordnik.swagger.config.SwaggerConfig;

public interface RestResourceConfig {


	void setSwaggerConfig(SwaggerConfig swaggerConfig);

	SwaggerConfig getSwaggerConfig();

	void setModelPackage(String modelPackage);

	String getModelPackage();



}
