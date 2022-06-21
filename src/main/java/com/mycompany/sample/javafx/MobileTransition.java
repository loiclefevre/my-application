package com.mycompany.sample.javafx;

import javafx.animation.Transition;

public abstract class MobileTransition extends Transition {
	private final Runnable pauseListener = () -> {
		this.pause();
	};
	private final Runnable resumeListener = () -> {
		this.play();
	};

	public MobileTransition() {
		this.statusProperty().addListener((ov, t, newStatus) -> {
			switch(newStatus) {
				case RUNNING:
					this.starting();
					break;
				case STOPPED:
					this.stopping();
			}

		});
	}

	protected void starting() {
		LifecycleService.create().ifPresent((service) -> {
			service.addListener(LifecycleEvent.PAUSE, this.pauseListener);
			service.addListener(LifecycleEvent.RESUME, this.resumeListener);
		});
	}

	protected void stopping() {
		LifecycleService.create().ifPresent((service) -> {
			service.removeListener(LifecycleEvent.PAUSE, this.pauseListener);
			service.removeListener(LifecycleEvent.RESUME, this.resumeListener);
		});
	}
}
