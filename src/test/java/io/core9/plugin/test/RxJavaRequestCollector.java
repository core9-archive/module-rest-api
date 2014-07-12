package io.core9.plugin.test;

import io.core9.plugin.rest.RestRequest;

import java.util.List;
import java.util.Map;

public interface RxJavaRequestCollector {

	RxJavaRequestCollector setRequest(RestRequest request);

	List<Map<String, RestRequest>> getRequests(String rxJavaMethod);

	Map<String, List<Map<String, RestRequest>>> getRequests();






}
