package io.core9.plugin.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.core9.plugin.rest.RestRequest;

public class RxJavaRequestCollectorImpl implements RxJavaRequestCollector {

	Map<String, List<Map<String, RestRequest>>> requests = new HashMap<String, List<Map<String, RestRequest>>>();

	@Override
	public RxJavaRequestCollector setRequest(RestRequest request) {

		Map<String, RestRequest> newRequest = new HashMap<String, RestRequest>();
		newRequest.put(request.getRxJavaVarName(), request);

		if (requests.containsKey(request.getRxJavaMethod())) {
			List<Map<String, RestRequest>> existingList = requests.get(request.getRxJavaMethod());
			existingList.add(newRequest);
			requests.put(request.getRxJavaMethod(), existingList);
		} else {
			List<Map<String, RestRequest>> list = new ArrayList<Map<String, RestRequest>>();
			list.add(newRequest);
			requests.put(request.getRxJavaMethod(), list);
		}

		return this;
	}

	@Override
	public List<Map<String, RestRequest>> getRequests(String rxJavaMethod) {
		return requests.get(rxJavaMethod);
	}

	@Override
	public  Map<String, List<Map<String, RestRequest>>> getRequests() {
		return requests;
	}

}
