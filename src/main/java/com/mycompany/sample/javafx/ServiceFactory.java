package com.mycompany.sample.javafx;

import java.util.Optional;

public interface ServiceFactory<S> {
	Class<S> getServiceType();

	Optional<S> getInstance();
}