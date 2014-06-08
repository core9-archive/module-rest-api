package io.core9.plugin.rest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

@PluginImplementation
public class RestRouterImpl implements RestRouter {

	@InjectPlugin
	private RestResourceModuleRegistry restResourceModuleRegistry;
	@SuppressWarnings("unused")
	private RestRequest restRequest;

	private JSONObject getResponse(RestRequest request, String basePath, String requestPath, String requestMethod, String arg1, String arg2) {

		JSONObject result = new JSONObject();

		RestResource apiResource;
		JSONObject apiJson;
		if (ifApiRequest(requestPath)) {
			apiResource = restResourceModuleRegistry.getResource(getApiPath(requestPath));
			apiJson = apiResource.getApi();
			return apiJson;
		}

		apiResource = restResourceModuleRegistry.getResource(requestPath);
		apiJson = apiResource.getApi();
		JSONArray apis = (JSONArray) apiJson.get("apis");
		Object apiObject = apiResource.getResourceObject();

		for (Object api : apis) {

			JSONObject jsonObj = (JSONObject) api;
			String method = (String) ((JSONObject) ((JSONArray) jsonObj.get("operations")).get(0)).get("nickname");
			String path = (String) jsonObj.get("path");
			String[] pathParts = path.split("\\{");

			switch (request.getMethod()) {
			case DELETE:
				break;
			case PUT:
				break;
			case GET:
				result = handleGet(result, apiObject, method, arg1, arg2, pathParts);
				break;
			case POST:
				break;
			case HEAD:
				break;
			case OPTIONS:
				break;
			default:
				System.out.println("method not catched");
			}

		}

		return result;
	}

	private JSONObject handleGet(JSONObject result, Object apiObject, String method, String arg1, String arg2, String[] pathParts) {
		if (pathParts.length > 1) {
			// arg 1 is id
			try {
				result = RestUtils.getResultFromRequest(apiObject, method, arg1);
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (method.equals(arg1)) {
			try {
				result = RestUtils.getResultFromRequest(apiObject, method, arg2);
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private String getApiPath(String apiPath) {
		String[] apiRequest = apiPath.split("-");
		return apiRequest[0];
	}

	private boolean ifApiRequest(String apiPath) {
		String[] apiRequest = apiPath.split("-");
		if (apiRequest.length == 1) {
			return false;
		}
		if ("docs".equals(apiRequest[1])) {
			return true;
		}

		return false;
	}

	@Override
	public JSONObject getResponse(RestRequest req) {
		this.restRequest = req;

		String arg1 = null;
		String arg2 = null;
		return getResponse(req, req.getBasePath(), req.getPath(), req.getMethod().name(), arg1, arg2);

	}

}
