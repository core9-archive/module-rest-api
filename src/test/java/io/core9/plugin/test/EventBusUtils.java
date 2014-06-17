package io.core9.plugin.test;

import io.core9.plugin.rest.RestRequest;
import io.core9.plugin.rest.RestRequestImpl;
import io.core9.plugin.rest.RestRouter;
import io.vertx.rxcore.java.eventbus.RxEventBus;
import io.vertx.rxcore.java.eventbus.RxMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.vertx.java.core.Vertx;
import org.vertx.java.core.VertxFactory;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;

public class EventBusUtils {

	private Vertx vertx = VertxFactory.newVertx();
	private final RxEventBus rxEventBus = new RxEventBus(vertx.eventBus());
	private HashMap<String, Object> processedResult;
	private RestRouter restRouter;

	private static final EventBusUtils INSTANCE = new EventBusUtils();

	private EventBusUtils() {
	}

	public static EventBusUtils getInstance() {
		return INSTANCE;
	}
	

	public EventBusUtils setRestRouter(RestRouter restRouter){
		
		this.restRouter = restRouter;
		return this;
	}

	public Map<String, Object> procesRequest(Map<String, Map<String, RestRequest>> collection) {
		
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
		
		Map<String, RestRequest> parallelCollection = collection.get("parallel");
		
		if(parallelCollection.size() > 0){
			result.put("parallel", procesParallel(parallelCollection));
		}
		
		/*
		 * make this also a parallel result and add to parallel
		 * if(collection.get("serialize").size() > 0){
		}*/
		
		
		
		return null;
	}

	private Map<String, Object> procesParallel(Map<String, RestRequest> parallelMap) {

		rxEventBus.<String> registerHandler("parallel").subscribe(new Action1<RxMessage<String>>() {
			@Override
			public void call(RxMessage<String> msg) {
				
				RestRequest request = new RestRequestImpl();
				request.fromJsonString(msg.body());

				@SuppressWarnings("unused")
				Object response = restRouter.getResponse(request);
				//msg.reply("pong" + msg.body());
				msg.reply("pong" + msg.body());
			}
		});
		
		
		List<Observable<RxMessage<String>>> args = new ArrayList<Observable<RxMessage<String>>>();
		
		for(Entry<String, RestRequest> job : parallelMap.entrySet()){
			
			String jobRequest = job.getValue().toJson().toString();
			
			Observable<RxMessage<String>> obs1 = rxEventBus.send("parallel", jobRequest);
			
			args.add(obs1);
		}

		/*Observable<RxMessage<String>> obs1 = rxEventBus.send("parallel", "A");
		Observable<RxMessage<String>> obs2 = rxEventBus.send("parallel", "B");
		Observable<RxMessage<String>> obs3 = rxEventBus.send("parallel", "C");*/
		
		
		
		
		Observable<RxMessage<String>> merged = Observable.merge(args);

		Observable<String> result = merged.reduce("", new Func2<String, RxMessage<String>, String>() {
			

			@Override
			public String call(String accum, RxMessage<String> reply) {
				
				JSONObject mes = (JSONObject)JSONValue.parse(reply.body());
				
				processedResult.put((String) mes.get("rxJavaVarName"), mes);
				
				return accum + reply.body();
			}
		});

		//RxAssert.assertSequenceThenComplete(result.takeLast(1), "pongApongBpongC");

		System.out.println(result.takeLast(1));
		
		return processedResult;
		
	}

}
