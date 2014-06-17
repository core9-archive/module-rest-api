package io.core9.plugin.test;


import scala.Option;

import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.core.util.JsonSerializer;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.model.ApiListing;
import com.wordnik.swagger.sample.resource.PetResource;

public class TestBasicResource {

	
	//@Test
	public void test(){
	

	    DefaultJaxrsApiReader reader = new DefaultJaxrsApiReader();
	    SwaggerConfig config = new SwaggerConfig();
	    config.setApiVersion("1.0.1");
	    config.setBasePath("http://localhost:8080/api");
	    
	    Option<ApiListing> apiResource = reader.read("/api-docs", PetResource.class, config);
	    

	    String json = JsonSerializer.asJson(apiResource);
	    System.out.println(json);
	    System.out.println("end");
	}
		
	
}
