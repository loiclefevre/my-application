package com.mycompany.sample.javafx;

import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class NoTransition extends CachedTimelineTransition {
	public NoTransition() {
		super((Node)null, new Timeline());
		this.setCycleDuration(Duration.seconds(0.001D));
	}

	protected void starting() {
	}

	protected void stopping() {
	}

	protected void interpolate(double d) {
	}
}
