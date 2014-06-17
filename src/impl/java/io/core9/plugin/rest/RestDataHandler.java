package io.core9.plugin.rest;

import io.core9.core.plugin.Core9Plugin;
import io.core9.plugin.widgets.datahandler.DataHandlerFactory;
import io.core9.plugin.widgets.datahandler.DataHandlerFactoryConfig;

public interface RestDataHandler<T extends DataHandlerFactoryConfig> extends DataHandlerFactory<T>, Core9Plugin {

}
