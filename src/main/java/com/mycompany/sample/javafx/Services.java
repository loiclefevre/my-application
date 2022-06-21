package com.mycompany.sample.javafx;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class Services<T> {
	private static final Logger LOGGER = Logger.getLogger(Services.class.getName());
	private static final Map<Class, ServiceFactory> FACTORY_MAP = new HashMap();
	private static final Map<Class, Object> SERVICE_MAP = new HashMap();

	private Services() {
	}

	public static <T> void registerServiceFactory(ServiceFactory<T> factory) {
		LOGGER.fine("Register " + factory);
		FACTORY_MAP.put(factory.getServiceType(), factory);
	}

	public static <T> Optional<T> get(Class<T> service) {
		LOGGER.fine("Get Service " + service.getName());
		if (!FACTORY_MAP.containsKey(service)) {
			ServiceFactory<T> factory = getFactory(service);
			if (factory == null) {
				throw new RuntimeException("The service " + service.getSimpleName() + " can't be registered. Call Services.registerServiceFactory() with a valid ServiceFactory");
			}

			registerServiceFactory(factory);
		}

		if (!SERVICE_MAP.containsKey(service)) {
			((ServiceFactory)FACTORY_MAP.get(service)).getInstance().ifPresent((t) -> {
				SERVICE_MAP.put(service, t);
			});
		}

		LOGGER.fine("Return service: " + SERVICE_MAP.get(service));
		return Optional.ofNullable((T)SERVICE_MAP.get(service));
	}

	private static <T> ServiceFactory<T> getFactory(Class<T> service) {
		return new DefaultServiceFactory(service);
	}
}
