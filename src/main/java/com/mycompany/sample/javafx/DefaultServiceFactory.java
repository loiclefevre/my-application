package com.mycompany.sample.javafx;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultServiceFactory<T> implements ServiceFactory<T> {
	private static final Logger LOGGER = Logger.getLogger(DefaultServiceFactory.class.getName());
	private final Class<T> serviceType;
	private T instance;

	public DefaultServiceFactory(Class<T> serviceType) {
		this.serviceType = serviceType;
	}

	public Class<T> getServiceType() {
		return this.serviceType;
	}

	public Optional<T> getInstance() {
		if (this.instance == null) {
			this.instance = this.createInstance("DESKTOP");
		}

		return Optional.ofNullable(this.instance);
	}

	private T createInstance(String platform) {
		String var10000 = this.serviceType.getPackageName();
		String fqn = var10000 + ".impl." + this.className(platform);

		try {
			Class<T> clazz = (Class<T>)Class.forName(fqn);
			if (clazz != null) {
				LOGGER.fine("Service class for: " + clazz.getName());
				return clazz.getDeclaredConstructor().newInstance();
			}
		} catch (IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException | InstantiationException var4) {
			LOGGER.log(Level.SEVERE, (String)null, var4);
		} catch (ClassNotFoundException var5) {
			LOGGER.log(Level.WARNING, "No new instance for " + this.serviceType + " and class " + fqn);
		}

		return null;
	}

	private String className(String platform) {
		return platform + this.serviceType.getSimpleName();
	}
}
