package io.core9.plugin.rest;

import io.core9.core.plugin.Core9Plugin;

import java.util.Map;

public interface RestResourceProvider extends Core9Plugin {

	
	Map<String, RestResource> getResources();
	
}
