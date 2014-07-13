package io.core9.plugin.rest;

import io.core9.core.boot.CoreBootStrategy;

import java.util.List;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

import org.apache.commons.lang3.ClassUtils;

@PluginImplementation
public class RestBootStrategyImpl extends CoreBootStrategy implements
		RestBootStrategy {

	@InjectPlugin
	private RestResourceModuleRegistry restResourceModuleRegistry;

	@Override
	public void processPlugins() {
		for (Plugin plugin : this.registry.getPlugins()) {

			List<Class<?>> interfaces = ClassUtils.getAllInterfaces(plugin
					.getClass());
			if (interfaces.contains(RestResourceProvider.class)) {

				restResourceModuleRegistry
						.setResources(((RestResourceProvider) plugin)
								.getResources());
			}

		}
		System.out.print("pause");
	}

	@Override
	public Integer getPriority() {
		return 600;
	}

}
