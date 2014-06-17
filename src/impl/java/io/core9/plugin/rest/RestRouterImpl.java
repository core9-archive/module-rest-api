package io.core9.plugin.rest;

import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

import com.sun.jersey.api.uri.UriTemplate;

@PluginImplementation
public class RestRouterImpl implements RestRouter {

	@InjectPlugin
	private RestResourceModuleRegistry restResourceModuleRegistry;

	public Object getResponse(RestRequest request) {

		if (RestUtils.ifApiRequest(request.getPath())) {
			return restResourceModuleRegistry.getResource(RestUtils.getApiPath(request.getPath())).getApi();
		}

		Object result = null;
		Object resourceObject = restResourceModuleRegistry.getResource(request.getApi()).getResourceObject();

		for (Map<String, Object> resource : restResourceModuleRegistry.getResourceMap(request.getHash())) {

			System.out.println((String) resource.get("normalizedTemplate"));

			Map<String, String> map = new HashMap<String, String>();
			UriTemplate template = new UriTemplate((String) resource.get("normalizedTemplate"));
			if (template.match(request.getPath(), map)) {
				System.out.println("Matched, " + map);
				result = RestUtils.getResultFromRequest(resourceObject, request, resource, map);
				break;
			} else {
				System.out.println("No match");
			}

		}

		return result;
	}

}
