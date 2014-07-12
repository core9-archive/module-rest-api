package io.core9.plugin.rest;

import io.core9.plugin.server.Cookie;
import io.core9.plugin.server.VirtualHost;
import io.core9.plugin.server.request.Method;
import io.core9.plugin.server.request.Request;
import io.core9.plugin.server.request.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;



import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestRequestImpl implements RestRequest {
	private static HashMap<String, Method> methods = new HashMap<String, Method>();
	{
		methods.put("GET", Method.GET);
		methods.put("PUT", Method.PUT);
		methods.put("POST", Method.POST);
		methods.put("DELETE", Method.DELETE);
		methods.put("OPTIONS", Method.OPTIONS);
		methods.put("HEAD", Method.HEAD);
	}

	public enum RxJavaMethod {
		SERIAL, MERGE
	};

	Request request;
	private VirtualHost vhost;
	private Response response;
	private String path;
	private Method type;
	private Map<String, Object> context;
	private Map<String, Object> params = new HashMap<String, Object>();
	private String body;
	private List<Cookie> cookies;
	private Map<String, Object> bodyAsMap;
	private List<Object> bodyAsList;
	private String basePath;
	private String[] pathParts;
	private String rxJavaMethod;
	private String rxJavaVarName;

	@Override
	public List<Cookie> getAllCookies(String name) {
		List<Cookie> foundCookies = new ArrayList<>();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (name.equals(c.getName())) {
					foundCookies.add(c);
				}
			}
		}
		return foundCookies;
	}

	@Override
	public String getBasePath() {
		return basePath;
	}

	@Override
	public String getBody() {
		return body;
	}

	@Override
	public List<Object> getBodyAsList() {

		if (bodyAsList != null) {
			return bodyAsList;
		}
		return null;
	}

	@Override
	public Map<String, Object> getBodyAsMap() {

		if (bodyAsMap != null) {
			return bodyAsMap;
		}
		
		return null;
	}

	@Override
	public Map<String, Object> getContext() {
		return this.context;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> R getContext(String name) {
		return (R) this.context.get(name);
	}

	@Override
	public <R> R getContext(String name, R defaultValue) {
		if (context.containsKey(name)) {
			return getContext(name);
		} else {
			return defaultValue;
		}
	}

	@Override
	public Cookie getCookie(String name) {
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (name.equals(c.getName()) && ("/".equals(c.getPath()) || c.getPath() == null)) {
					return c;
				}
			}
		}
		return null;
	}

	@Override
	public List<Cookie> getCookies() {
		return cookies;
	}

	@Override
	public String getHostname() {
		return request.getHostname();
	}

	@Override
	public Method getMethod() {
		return type;
	}

	@Override
	public Map<String, Object> getParams() {
		return this.params;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public String getPathPart(int part) {
		return pathParts[part];
	}

	@Override
	public int getPathPartNr() {
		return pathParts.length;
	}

	@Override
	public Response getResponse() {
		return response;
	}

	@Override
	public VirtualHost getVirtualHost() {
		return this.vhost;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> R putContext(String name, R value) {
		return (R) this.context.put(name, value);
	}

	@Override
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	@Override
	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public void setBodyAsList(List<Object> bodyAsList) {
		this.bodyAsList = bodyAsList;
	}

	@Override
	public void setBodyAsMap(Map<String, Object> bodyAsMap) {
		this.bodyAsMap = bodyAsMap;
	}

	@Override
	public void setCookies(List<Cookie> cookies) {
		this.cookies = cookies;
	}

	@Override
	public void setMethod(Method method) {
		type = method;
	}

	@Override
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	@Override
	public void setPath(String path) {
		String[] tmpPathParts2 = path.split("\\?");
		if (tmpPathParts2.length > 1) {
			String queryString = tmpPathParts2[1];
			String[] paramsParts = queryString.split("&");
			for (String param : paramsParts) {
				String[] theParam = param.split("=");
				getParams().put(theParam[0], theParam[1]);
			}
		}

		String[] basePathParts = basePath.split("/");
		String[] tmpPathParts = tmpPathParts2[0].split("/");

		pathParts = Arrays.copyOfRange(tmpPathParts, basePathParts.length, tmpPathParts.length);
		this.path = tmpPathParts2[0].substring(basePath.length());
		System.out.println("");
	}

	@Override
	public void setVirtualHost(VirtualHost virtualHost) {
		this.vhost = virtualHost;
	}

	@Override
	public JSONObject toJson() {
		JSONObject json = new JSONObject();


		json.put("pathParts", pathParts);
		json.put("basePath", basePath);
		json.put("method", type.name());
		json.put("path", path);
		json.put("params", new JSONObject(params));
		if(vhost != null){
			json.put("host", vhost.getHostname());	
		}
		json.put("rxJavaVarName", rxJavaVarName);
		json.put("rxJavaMethod", rxJavaMethod);
		
		return json;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void fromJson(JSONObject json) {
		this.path = (String) json.get("path");
		this.rxJavaVarName = (String) json.get("rxJavaVarName");
		this.rxJavaMethod = (String) json.get("rxJavaMethod");
		//this.vhost = new VirtualHost((String) json.get("host"));
		this.type = methods.get((String) json.get("method"));

		JSONObject tmp = (JSONObject) json.get("params");
		HashMap<String, Object> result = null;
		try {
			result = new ObjectMapper().readValue(tmp.toJSONString(), HashMap.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Object parts = json.get("pathParts");
		
		JSONArray parts = (JSONArray)JSONValue.parse(json.get("pathParts").toString());

		String[] arr = new String[parts.size()];
		for(int i = 0; i < parts.size(); i++){
		   arr[i] = (String) parts.get(0);
		}
		
		this.pathParts = arr;
		this.basePath = (String) json.get("basePath");
		this.params = result;

	}

	@Override
	public String getHash() {
		return path.split("/")[1] + type.name() + pathParts.length;// petPUT2
	}

	@Override
	public String getApi() {
		return "/" + pathParts[0];
	}

	@Override
	public void setRxJavaMethod(String rxJavaMethod) {
		this.rxJavaMethod = rxJavaMethod;
	}

	@Override
	public void setRxJavaVarName(String rxJavaVarName) {
		this.rxJavaVarName = rxJavaVarName;
	}

	@Override
	public String getRxJavaMethod() {
		return rxJavaMethod;
	}

	@Override
	public String getRxJavaVarName() {
		return rxJavaVarName;
	}

	@Override
	public void fromJsonString(String jsonString) {
		JSONObject json = (JSONObject) JSONValue.parse(jsonString);
		fromJson(json);
	}

}