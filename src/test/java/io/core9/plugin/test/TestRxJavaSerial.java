package io.core9.plugin.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import io.core9.core.PluginRegistry;
import io.core9.core.PluginRegistryImpl;
import io.core9.core.boot.BootstrapFramework;
import io.core9.plugin.rest.RestRequest;
import io.core9.plugin.rest.RestRequestImpl;
import io.core9.plugin.rest.RestResourceRunRxJavaProcess;
import io.core9.plugin.rest.RestRouter;
import io.core9.plugin.rest.RestRouterImpl;
import io.core9.plugin.server.request.Method;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class TestRxJavaSerial {

	private static PluginRegistry registry;
	private static RestRouter restRouter;



	public void runRestRequestTestSerial() {

		final RestRequest request = new RestRequestImpl();

		request.setBasePath("/api");
		request.setMethod(Method.GET);
		request.setPath("/api/pet-docs");
		request.setRxJavaVarName("var");

		final RestRequest request1 = new RestRequestImpl();

		request1.setBasePath("/api");
		request1.setMethod(Method.GET);
		request1.setPath("/api/pet-docs");
		request1.setRxJavaVarName("var1");

		List<Object> args = new ArrayList<Object>();
		args.add(request);
		args.add(request1);
		JSONObject result = RestResourceRunRxJavaProcess.runSerial(restRouter, args);
		
		System.out.println(result);
	}

	public void setUp() {
		BootstrapFramework.run();
		registry = PluginRegistryImpl.getInstance();
		restRouter = (RestRouter) registry.getPlugin(RestRouterImpl.class);

		assertNotNull(restRouter);
	}

	@SuppressWarnings("unchecked")
	public void restRouterGetApiForPet() {

		RestRequest request = new RestRequestImpl();

		request.setBasePath("/api");
		request.setMethod(Method.GET);
		request.setPath("/api/pet-docs");

		Object response = restRouter.getResponse(request);

		assertTrue(((HashMap<String, Object>) response).get("resourcePath").equals("/pet"));
	}

	public void restRouterGetPetById() {

		RestRequest request = new RestRequestImpl();

		request.setBasePath("/api");
		request.setMethod(Method.GET);
		request.setPath("/api/pet/1");

		Object response = restRouter.getResponse(request);
		JSONObject jsonResponse = (JSONObject) JSONValue.parse(response.toString());
		assertTrue("Cat 1".equals(jsonResponse.get("name")));
		System.out.println("");
	}

	public void restRouterGetfindByTags() {

		RestRequest request = new RestRequestImpl();

		request.setBasePath("/api");
		request.setMethod(Method.GET);
		request.setPath("/api/pet/findByTags?tags=tag1");

		JSONArray response = (JSONArray) JSONValue.parse(restRouter.getResponse(request).toString());

		assertTrue("Cat 1".equals(((JSONObject) response.get(0)).get("name")));

		System.out.println(response);
	}

	public void restRouterGetOwnerOfPet() {

		RestRequest request = new RestRequestImpl();

		request.setBasePath("/api");
		request.setMethod(Method.GET);
		request.setPath("/api/pet/1/owner");

		JSONObject response = (JSONObject) JSONValue.parse(restRouter.getResponse(request).toString());

		assertTrue("Tony".equals(response.get("name")));

		System.out.println(response);
	}

	public void restRouterPutPet() {

		RestRequest request = new RestRequestImpl();

		request.setBasePath("/api");
		request.setMethod(Method.PUT);
		request.setPath("/api/pet");

		JSONObject body = (JSONObject) JSONValue.parse(readFile("TestRestRouter.restRouterPostPet.json"));

		request.setBody(body.toString());

		String response = (String) restRouter.getResponse(request);

		assertTrue("\"SUCCESS\"".equals(response));

		System.out.println(response);
	}

	public void restRouterPostPet() {

		RestRequest request = new RestRequestImpl();

		request.setBasePath("/api");
		request.setMethod(Method.POST);
		request.setPath("/api/pet");

		JSONObject body = (JSONObject) JSONValue.parse(readFile("TestRestRouter.restRouterPostPet.json"));

		request.setBody(body.toString());

		String response = (String) restRouter.getResponse(request);

		assertTrue("\"SUCCESS\"".equals(response));

		System.out.println(response);
	}

	public static void main(String[] args) {

		TestRxJavaSerial routerTest = new TestRxJavaSerial();
		routerTest.setUp();
		long start = System.currentTimeMillis();
		routerTest.restRouterGetApiForPet();
		routerTest.restRouterGetPetById();
		routerTest.restRouterGetfindByTags();
		routerTest.restRouterGetOwnerOfPet();
		routerTest.restRouterPostPet();
		routerTest.restRouterPutPet();

		routerTest.runRestRequestTestSerial();

		long elapsed = System.currentTimeMillis() - start;
		System.out.println("elapsed time = " + elapsed + "ms");
		System.out.println((elapsed * 1000.0) / 1000000 + " microseconds per execution");

		System.exit(0);
	}

	static String readFile(String file) {
		URL main = TestRxJavaSerial.class.getResource(file);
		File path = new File(main.getPath());
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(path.getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(encoded, StandardCharsets.UTF_8);
	}

}
