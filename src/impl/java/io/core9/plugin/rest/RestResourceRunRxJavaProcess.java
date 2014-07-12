package io.core9.plugin.rest;

import java.util.List;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class RestResourceRunRxJavaProcess {

	public static JSONObject runParallel(final RestRouter restRouter, List<Object> args) {
		final JSONObject jsonResult = new JSONObject();
		long t1 = System.nanoTime();
		Observable.from(args).parallel(new Func1<Observable<Object>, Observable<String>>() {
			@Override
			public Observable<String> call(Observable<Object> t1) {
				return t1.map(new Func1<Object, String>() {
					@Override
					public String call(Object t1) {
						RestRequest req = (RestRequest) t1;
						Object response = restRouter.getResponse(req);
						JSONObject jsonResponse = new JSONObject();
						jsonResponse.put("rxJavaVarName", req.getRxJavaVarName());
						jsonResponse.put("reponse", response);
						return jsonResponse.toString();
					}
				});
			}
		}).toBlocking().forEach(new Action1<String>() {
			@Override
			public void call(String thingy) {
				JSONObject tmp = (JSONObject) JSONValue.parse(thingy);
				jsonResult.put((String) tmp.get("rxJavaVarName"), tmp);
			}
		});
		System.out.println("parallel test completed ----------");
		long t2 = System.nanoTime();
		System.out.println("Execution time: " + ((t2 - t1) * 1e-6) + " milliseconds");
		return jsonResult;
	}

	public static JSONObject runSerial(RestRouter restRouter, List<Object> args) {

		return null;
	}

}
