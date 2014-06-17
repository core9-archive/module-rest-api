package io.core9.plugin.rest;

import io.core9.plugin.server.Cookie;
import io.core9.plugin.server.VirtualHost;
import io.core9.plugin.server.request.Method;
import io.core9.plugin.server.request.Response;

import java.util.List;
import java.util.Map;

import net.minidev.json.JSONObject;

public interface RestRequest {
	/**
	 * Return the path
	 * @return
	 */
	String getPath();
	
	void setPath(String path);
	
	/**
	 * Get all set params
	 * @return
	 */
	Map<String,Object> getParams();
	
	void setParams(Map<String,Object> params);
	
	/**
	 * Get the response (wrapped) object
	 * @return
	 */
	Response getResponse();
	
	/**
	 * Get the request method (GET,POST,PUT etc)
	 * @return
	 */
	Method getMethod();
	
	void setMethod(Method method);
	
	/**
	 * Get the request body
	 */
	String getBody();
	
	void setBody(String body);
	
	/**
	 * Return the request body as a map
	 */
	Map<String, Object> getBodyAsMap();
	
	void setBodyAsMap(Map<String, Object> bodyAsMap);
	
	/**
	 * Return the request body as a list
	 */
	List<Object> getBodyAsList();
	
	void setBodyAsList(List<Object> bodyAsList);
	
	/**
	 * Get the request context
	 */
	Map<String, Object> getContext();
	
	/**
	 * Get a property from the context
	 */
	<R> R getContext(String name, R defaultVal);
	
	/**
	 * Get a property from the context
	 */
	<R> R getContext(String name);
	
	/**
	 * Add a property to the context
	 */
	<R> R putContext(String name, R value);
	
	/**
	 * Return the virtual host
	 */
	VirtualHost getVirtualHost();
	
	void setVirtualHost(VirtualHost virtualHost);
	
	/**
	 * Return a cookie by name
	 * @param name
	 * @return
	 */
	Cookie getCookie(String name);
	
	/**
	 * Return all cookies by name
	 * @param name
	 * @return
	 */
	List<Cookie> getAllCookies(String name);
	
	/**
	 * Set the cookies
	 * @param cookies
	 */
	void setCookies(List<Cookie> cookies);
	
	/**
	 * Get the hostname
	 */
	String getHostname();

	List<Cookie> getCookies();

	JSONObject toJson();

	void setBasePath(String basePath);

	String getBasePath();

	String getPathPart(int part);

	int getPathPartNr();

	String getHash();

	String getApi();

	void setRxJavaMethod(String rxJavaMethod);

	void setRxJavaVarName(String rxJavaVarName);
	
	String getRxJavaMethod();

	String getRxJavaVarName();



	void fromJsonString(String jsonString);

	void fromJson(JSONObject jsonStr);


}
