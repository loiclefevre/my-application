package com.mycompany.sample.javafx;

import java.util.Optional;

public interface LifecycleService {
	static Optional<LifecycleService> create() {
		return Services.get(LifecycleService.class);
	}

	void addListener(LifecycleEvent var1, Runnable var2);

	void removeListener(LifecycleEvent var1, Runnable var2);

	void shutdown();
}
