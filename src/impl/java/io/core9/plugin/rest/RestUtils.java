package io.core9.plugin.rest;

import io.core9.plugin.server.request.Request;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import scala.Option;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.core.util.JsonSerializer;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.model.ApiListing;

public class RestUtils {

	public static Map<String, RestResource> addRestResource(RestResourceConfig restResourceConfig, Object resourceObject) {

		Map<String, RestResource> resourceMap = new HashMap<>();

		RestResource restResource = new RestResourceImpl();

		restResource.setResourceObject(restResourceConfig.getSwaggerConfig(), resourceObject);
		restResource.setModelPackage(restResourceConfig.getModelPackage());

		resourceMap.put(RestUtils.getResourcePath(resourceObject.getClass()), restResource);

		return resourceMap;
	}

	public static JSONObject getApiFromResource(String apiVersion, String basePath, String apiPath, Class<?> clazz) {

		DefaultJaxrsApiReader reader = new DefaultJaxrsApiReader();
		SwaggerConfig config = new SwaggerConfig();
		config.setApiVersion(apiVersion);
		config.setBasePath(basePath);

		Option<ApiListing> apiResource = reader.read(apiPath, clazz, config);

		return (JSONObject) JSONValue.parse(JsonSerializer.asJson(apiResource));

	}

	public static String getResourcePath(Class<?> clazz) {
		return clazz.getAnnotation(javax.ws.rs.Path.class).value();
	}

	public static RestRequest convertServerRequestToRestRequest(String basePath, Request request) {

		RestRequest req = new RestRequestImpl();
		req.setBody(request.getBody());
		req.setMethod(request.getMethod());
		req.setBasePath(basePath);
		req.setPath(request.getPath());
		req.setParams(request.getParams());
		req.setVirtualHost(request.getVirtualHost());
		return req;
	}

	public static String getApiPath(String apiPath) {
		String[] apiRequest = apiPath.split("-");
		return apiRequest[0];
	}

	public static boolean ifApiRequest(String apiPath) {
		String[] apiRequest = apiPath.split("-");
		if (apiRequest.length == 1) {
			return false;
		}
		if ("docs".equals(apiRequest[1])) {
			return true;
		}

		return false;
	}

	public static Object getResultFromRequest(Object resourceObject, RestRequest request, Map<String, Object> resourceMap, Map<String, String> urlParam) {

		ObjectMapper MAPPER = new ObjectMapper();

		MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		//validateAndInitiateMethodBasedOnParameters(resourceObject, request, resourceMap, urlParam);

		JSONArray methodParameters = (JSONArray) resourceMap.get("parameters");
		@SuppressWarnings("rawtypes")
		Class[] paramTypes = new Class[methodParameters.size()];
		Object[] args = new Object[methodParameters.size()];

		int i = 0;
		for (Object param : methodParameters) {

			JSONObject objParam = (JSONObject) param;

			String p = (String) objParam.get("type");

			if ("body".equals((String) objParam.get("paramType"))) {

				String clazz = (String) resourceMap.get("modelPackage") + "." + (String) objParam.get("type");
				String body = request.getBody();

				Object classObject = null;
				try {
					classObject = MAPPER.readValue(body, Class.forName(clazz));

					System.out.println("tmp");
				} catch (JsonParseException e) {

					e.printStackTrace();
				} catch (JsonMappingException e) {

					e.printStackTrace();
				} catch (ClassNotFoundException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}

				args[i] = classObject;

				try {
					paramTypes[i] = Class.forName(clazz);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				i++;
			}

			if ("path".equals((String) objParam.get("paramType"))) {

				String paramName = (String) objParam.get("name");
				args[i] = urlParam.get(paramName);

				switch (p) {
				case "string":
					paramTypes[i] = String.class;
					i++;
					break;

				}
			}

			if ("query".equals((String) objParam.get("paramType"))) {

				String paramName = (String) objParam.get("name");
				args[i] = request.getParams().get(paramName);

				switch (p) {
				case "string":
					paramTypes[i] = String.class;
					i++;
					break;

				}
			}

			System.out.println(objParam);

		}

		Response response = null;
		Object methodObj = null;
		try {
			methodObj = resourceObject.getClass().getMethod((String) resourceMap.get("nickname"), paramTypes);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		try {
			response = (Response) ((Method) methodObj).invoke(resourceObject, args);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		Object entity = response.getEntity();
		String jsonString = null;

		try {
			jsonString = PojoMapper.toJson(entity, true);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Object result = jsonString;

		return result;
	}



}
