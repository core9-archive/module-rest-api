package io.core9.plugin.test.async;

import static org.vertx.testtools.VertxAssert.testComplete;
import io.vertx.rxcore.java.eventbus.RxEventBus;
import io.vertx.rxcore.java.eventbus.RxMessage;

import org.junit.Test;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VertxFactory;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.testtools.TestVerticle;

import rx.Observable;
import rx.functions.Action1;

public class AgainVertx extends TestVerticle {

	private Vertx vertx = VertxFactory.newVertx();

	private EventBus eb = vertx.eventBus();

	@SuppressWarnings("rawtypes")
	@Test
	public void testVertx() {

		Handler<Message> myHandler = new Handler<Message>() {
			public void handle(Message message) {
				System.out.println("I received a message " + message.body());
			}
		};

		eb.registerHandler("test.address", myHandler);

		eb.publish("test.address", "hello world");

		System.out.print("pause" + "\n");
		 testComplete();
	}

	@Test
	public void testEventbusRx() {

		RxEventBus rxEventBus = new RxEventBus(vertx.eventBus());
		rxEventBus.<String> registerHandler("foo").subscribe(
				new Action1<RxMessage<String>>() {
					public void call(RxMessage<String> message) {
						
						System.out.println("Message is : "
								+ message.coreMessage().body());
						// Send a single reply
						message.reply("pong!");
					}
				});

		Observable<RxMessage<String>> obs = rxEventBus.send("foo", "ping!");

		obs.subscribe(new Action1<RxMessage<String>>() {
			public void call(RxMessage<String> message) {
				// Handle response
				System.out.println("Message is : "
						+ message.coreMessage().body());
			}
		}, new Action1<Throwable>() {
			public void call(Throwable err) {
				// Handle error
			}
		});
		 testComplete();
	}
	
	


}
