package com.mycompany.sample.javafx;

import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.BorderPane;

import java.util.Optional;
import java.util.function.Function;

@DefaultProperty("center")
public class View extends BorderPane {
	private final ReadOnlyBooleanWrapper showingProperty;
	private final ObjectProperty<Function<View, MobileTransition>> showTransitionFactoryProperty;
	private final ObjectProperty<EventHandler<LifecycleEvent>> onShowing;
	private final ObjectProperty<EventHandler<LifecycleEvent>> onShown;
	private final ObjectProperty<EventHandler<LifecycleEvent>> onHiding;
	private final ObjectProperty<EventHandler<LifecycleEvent>> onHidden;
	private final ObjectProperty<EventHandler<LifecycleEvent>> onCloseRequest;

	public View() {
		this((Node)null);
	}

	public View(Node content) {
		super(content);
		this.showingProperty = new ReadOnlyBooleanWrapper(this, "showing", true);
		this.showTransitionFactoryProperty = new SimpleObjectProperty(this, "showTransitionFactory");
		this.onShowing = new SimpleObjectProperty<>(this, "onShowing") {
			protected void invalidated() {
				View.this.setEventHandler(LifecycleEvent.SHOWING, this.get());
			}
		};
		this.onShown = new SimpleObjectProperty<>(this, "onShown") {
			protected void invalidated() {
				View.this.setEventHandler(LifecycleEvent.SHOWN, this.get());
			}
		};
		this.onHiding = new SimpleObjectProperty<>(this, "onHiding") {
			protected void invalidated() {
				View.this.setEventHandler(LifecycleEvent.HIDING, this.get());
			}
		};
		this.onHidden = new SimpleObjectProperty<>(this, "onHidden") {
			protected void invalidated() {
				View.this.setEventHandler(LifecycleEvent.HIDDEN, this.get());
			}
		};
		this.onCloseRequest = new SimpleObjectProperty<>(this, "onCloseRequest") {
			protected void invalidated() {
				View.this.setEventHandler(LifecycleEvent.CLOSE_REQUEST, this.get());
			}
		};
		this.getStyleClass().setAll(new String[]{"view"});
		this.addEventHandler(LifecycleEvent.SHOWING, (event) -> {
			this.onShowing();
		});
		this.addEventHandler(LifecycleEvent.HIDDEN, (event) -> {
			this.onHidden();
		});
		this.addEventHandler(LifecycleEvent.HIDING, (event) -> {
			this.requestFocus();
		});
	}

	private void onHidden() {
		this.showingProperty.set(false);
		if (this.getScene().getFocusOwner() instanceof TextInputControl) {
			this.requestFocus();
		}

	}

	private void onShowing() {
		this.showingProperty.set(true);
	}

	public final ReadOnlyBooleanProperty showingProperty() {
		return this.showingProperty.getReadOnlyProperty();
	}

	public final boolean isShowing() {
		return this.showingProperty.get();
	}

	public final ObjectProperty<Function<View, MobileTransition>> showTransitionFactoryProperty() {
		return this.showTransitionFactoryProperty;
	}

	public final Function<View, MobileTransition> getShowTransitionFactory() {
		return (Function)this.showTransitionFactoryProperty.get();
	}

	public final void setShowTransitionFactory(Function<View, MobileTransition> value) {
		this.showTransitionFactoryProperty.set(value);
	}

	public final ObjectProperty<EventHandler<LifecycleEvent>> onShowingProperty() {
		return this.onShowing;
	}

	public final void setOnShowing(EventHandler<LifecycleEvent> value) {
		this.onShowing.set(value);
	}

	public final EventHandler<LifecycleEvent> getOnShowing() {
		return (EventHandler)this.onShowing.get();
	}

	public final ObjectProperty<EventHandler<LifecycleEvent>> onShownProperty() {
		return this.onShown;
	}

	public final void setOnShown(EventHandler<LifecycleEvent> value) {
		this.onShown.set(value);
	}

	public final EventHandler<LifecycleEvent> getOnShown() {
		return (EventHandler)this.onShown.get();
	}

	public final ObjectProperty<EventHandler<LifecycleEvent>> onHidingProperty() {
		return this.onHiding;
	}

	public final void setOnHiding(EventHandler<LifecycleEvent> value) {
		this.onHiding.set(value);
	}

	public final EventHandler<LifecycleEvent> getOnHiding() {
		return (EventHandler)this.onHiding.get();
	}

	public final ObjectProperty<EventHandler<LifecycleEvent>> onHiddenProperty() {
		return this.onHidden;
	}

	public final void setOnHidden(EventHandler<LifecycleEvent> value) {
		this.onHidden.set(value);
	}

	public final EventHandler<LifecycleEvent> getOnHidden() {
		return (EventHandler)this.onHidden.get();
	}

	public final ObjectProperty<EventHandler<LifecycleEvent>> onCloseRequestProperty() {
		return this.onCloseRequest;
	}

	public final void setOnCloseRequest(EventHandler<LifecycleEvent> value) {
		this.onCloseRequest.set(value);
	}

	public final EventHandler<LifecycleEvent> getOnCloseRequest() {
		return (EventHandler)this.onCloseRequest.get();
	}

	public MobileTransition getShowTransition() {
		return (MobileTransition) Optional.ofNullable(this.getShowTransitionFactory()).map((factory) -> {
			return (MobileTransition)factory.apply(this);
		}).orElse(new NoTransition());
	}

	public final ApplicationManager getApplicationManager() {
		return ApplicationManager.getInstance();
	}

	public static class LifecycleEvent extends Event {
		public static final EventType<LifecycleEvent> ANY;
		public static final EventType<LifecycleEvent> SHOWING;
		public static final EventType<LifecycleEvent> SHOWN;
		public static final EventType<LifecycleEvent> HIDING;
		public static final EventType<LifecycleEvent> HIDDEN;
		public static final EventType<LifecycleEvent> CLOSE_REQUEST;

		public LifecycleEvent(EventTarget sourceAndTarget, EventType<? extends Event> eventType) {
			super(sourceAndTarget, sourceAndTarget, eventType);
		}

		static {
			ANY = new EventType(Event.ANY, "LIFECYCLE_EVENT");
			SHOWING = new EventType(ANY, "SHOWING");
			SHOWN = new EventType(ANY, "SHOWN");
			HIDING = new EventType(ANY, "HIDING");
			HIDDEN = new EventType(ANY, "HIDDEN");
			CLOSE_REQUEST = new EventType(ANY, "CLOSE_REQUEST");
		}
	}
}
