package io.core9.plugin.rest;

import io.core9.core.plugin.Core9Plugin;

public interface RestRouter extends Core9Plugin {

	Object getResponse(RestRequest req);

}
