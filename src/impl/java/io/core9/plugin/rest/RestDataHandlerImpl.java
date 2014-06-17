package io.core9.plugin.rest;

import io.core9.plugin.server.request.Request;
import io.core9.plugin.widgets.datahandler.DataHandler;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;

import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class RestDataHandlerImpl implements
		RestDataHandler<RestDataHandlerConfig> {




	@Override
	public String getName() {
		return "Rest-api";
	}

	private Map<String, Object> result = new HashMap<String, Object>();

	@Override
	public Class<? extends DataHandlerFactoryConfig> getConfigClass() {
		return RestDataHandlerConfig.class;
	}


	@Override
	public DataHandler<RestDataHandlerConfig> createDataHandler(
			final DataHandlerFactoryConfig options) {
		return new DataHandler<RestDataHandlerConfig>() {

			@Override
			public Map<String, Object> handle(Request request) {

				// return list with api's
				result.put("javascript", "");
				return result;
			}

			@Override
			public RestDataHandlerConfig getOptions() {
				return null;
			}


		};
	}

}
