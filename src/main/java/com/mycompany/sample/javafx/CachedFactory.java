package com.mycompany.sample.javafx;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.function.Supplier;

public class CachedFactory<T> {
	private Map<String, Supplier<T>> factories = new HashMap();
	private Map<String, T> cache = new WeakHashMap();

	public CachedFactory() {
	}

	public void put(String key, Supplier<T> supplier) {
		if (key != null) {
			if (supplier == null) {
				this.factories.remove(key);
				this.cache.remove(key);
			} else {
				this.factories.put(key, supplier);
			}

		}
	}

	public Optional<T> get(String key) {
		T value = this.cache.get(key);
		if (value == null && this.factories.containsKey(key)) {
			value = (T)((Supplier)this.factories.get(key)).get();
			this.cache.put(key, value);
		}

		return Optional.ofNullable(value);
	}

	public Optional<T> clearCache(String key) {
		return Optional.ofNullable(key == null ? null : this.cache.remove(key));
	}

	public boolean containsKey(String key) {
		return this.factories.containsKey(key);
	}

	public void remove(String key) {
		this.cache.remove(key);
		this.factories.remove(key);
	}
}
