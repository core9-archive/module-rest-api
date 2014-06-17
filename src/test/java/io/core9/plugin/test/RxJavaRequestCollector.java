package io.core9.plugin.test;

import java.util.Map;

import io.core9.plugin.rest.RestRequest;

public interface RxJavaRequestCollector {

	RxJavaRequestCollector setRequest(RestRequest request);

	Map<String, RestRequest> getRequests(String rxJavaMethod);

	Map<String, Map<String, RestRequest>> getRequests();





}
