package io.core9.plugin.test;

import java.util.HashMap;
import java.util.Map;

import io.core9.plugin.rest.RestRequest;

public class RxJavaRequestCollectorImpl implements RxJavaRequestCollector {

	Map<String, Map<String, RestRequest>> requests = new HashMap<String, Map<String, RestRequest>>();
	
	
	@Override
	public RxJavaRequestCollector setRequest(RestRequest request) {
		
		
		Map<String, RestRequest> newRequest = new HashMap<String, RestRequest>();
		newRequest .put(request.getRxJavaVarName(), request);
		 
		requests.put(request.getRxJavaMethod(), newRequest);
		return this;
	}


	@Override
	public Map<String, RestRequest> getRequests(String rxJavaMethod) {
		return requests.get(rxJavaMethod);
	}


	@Override
	public  Map<String, Map<String, RestRequest>> getRequests() {
		return requests;
	}

}
