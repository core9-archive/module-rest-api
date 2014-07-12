package io.core9.plugin.rest;

import io.core9.module.auth.AuthenticationPlugin;
import io.core9.plugin.server.HostManager;
import io.core9.plugin.server.Server;
import io.core9.plugin.server.handler.Middleware;
import io.core9.plugin.server.request.Request;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

@PluginImplementation
public class RestDispatcherImpl implements RestDispatcher {

	@InjectPlugin
	private Server server;

	@InjectPlugin
	private HostManager manager;

	@InjectPlugin
	private AuthenticationPlugin authentication;

	@InjectPlugin
	private RestRouter restRouter;

	@Override
	public void execute() {

		server.use("/api/:api((/:arg1)(/:arg2)?)?", new Middleware() {
			@Override
			public void handle(Request request) {

				Object result = restRouter.getResponse(RestUtils.convertServerRequestToRestRequest("/api", request));

				System.out.println("Result is : " + result);
				
				request.getResponse().putHeader("Access-Control-Allow-Origin", "*");
				request.getResponse().putHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
				request.getResponse().putHeader("Access-Control-Allow-Credentials", "true");
				request.getResponse().putHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
				request.getResponse().putHeader("Access-Control-Max-Age", "1209600");
				request.getResponse().end((String) result.toString());
			}
		});
	}
}
