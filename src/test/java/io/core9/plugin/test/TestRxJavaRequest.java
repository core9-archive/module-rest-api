package io.core9.plugin.test;

import io.core9.plugin.rest.RestRequest;
import io.core9.plugin.rest.RestRequestImpl;
import io.core9.plugin.server.VirtualHost;
import io.core9.plugin.server.request.Method;

import java.util.Map;

public class TestRxJavaRequest {

	
	
	//FIXME test this!!
	public void sendRequest(){

		RxJavaRequestCollector collector = new RxJavaRequestCollectorImpl();
		RestRequest request = new RestRequestImpl();

		
		request.setVirtualHost(new VirtualHost("localhost"));
		request.setBasePath("/api");
		request.setMethod(Method.GET);
		request.setPath("/api/pet-docs");
		request.setRxJavaMethod("parallel");
		request.setRxJavaVarName("petApi");

		

		collector.setRequest(request);
		collector.setRequest(request);
		Map<String, Map<String, RestRequest>> collection = collector.getRequests();
		
		@SuppressWarnings("unused")
		Map<String, Object> result = EventBusUtils.getInstance().procesRequest(collection);
		
		
		
	}
	
	
	
	
	
	
	
	
}
