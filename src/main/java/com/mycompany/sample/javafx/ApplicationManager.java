package com.mycompany.sample.javafx;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.Event;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ApplicationManager {
	public static final String HOME_VIEW = "home";

	private static ApplicationManager APPLICATION_MANAGER;

	public static ApplicationManager getInstance() {
		return APPLICATION_MANAGER;
	}

	public static ApplicationManager initialize(String title, Consumer<Scene> postInit) {
		if (APPLICATION_MANAGER != null) {
			throw new RuntimeException("The ApplicationManager was initialized already.");
		}
		else {
			return APPLICATION_MANAGER = new ApplicationManager(title, postInit);
		}
	}

	private final Consumer<Scene> postInit;
	private Stage primaryStage;
	private Scene primaryScene;
	private BorderPane borderPane;
	private final CachedFactory<View> viewFactory = new CachedFactory();
	private final String title;
	private final Deque<String> viewStack = new ArrayDeque();
	private final ReadOnlyObjectWrapper<View> viewProperty = new ReadOnlyObjectWrapper<View>((Object)null, "view") {
		private WeakReference<View> viewRef = null;

		public void invalidated() {
			View oldView = this.viewRef == null ? null : (View)this.viewRef.get();
			View newView = (View)this.get();
			if (oldView == null || oldView != newView) {
				if (oldView != null) {
					Event.fireEvent(oldView, new View.LifecycleEvent(oldView, View.LifecycleEvent.HIDDEN));
				}

				this.viewRef = new WeakReference(newView);
				if (newView != null) {
					if (ApplicationManager.this.borderPane == null) {
						throw new RuntimeException("The BorderPane was not initialized yet. Consider calling switchView() from postInit() instead");
					}

					ApplicationManager.this.borderPane.setCenter(newView);
					ApplicationManager.this.doSwitchView(ViewTools.findViewName(newView), ViewStackPolicy.USE);
					Event.fireEvent(newView, new View.LifecycleEvent(newView, View.LifecycleEvent.SHOWING));
				}

			}
		}
	};

	private ApplicationManager(String title, Consumer<Scene> postInit) {
		this.title=title;
		this.postInit = postInit;
	}

	public final void addViewFactory(String viewName, Supplier<View> supplier) {
		if (this.viewFactory.containsKey(viewName)) {
			throw new IllegalArgumentException("View with name '" + viewName + "' already exists - names must be unique");
		}
		else {
			this.viewFactory.put(viewName, supplier);
		}
	}

	public final double getScreenHeight() {
		return Screen.getPrimary().getVisualBounds().getHeight();
	}

	public final double getScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}

	public final void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.borderPane = new BorderPane();
		Dimension2D dim = new Dimension2D(this.getScreenWidth(), this.getScreenHeight());
		this.primaryScene = new Scene(this.borderPane, dim.getWidth(), dim.getHeight());
		primaryStage.setScene(this.primaryScene);
		this.switchView(HOME_VIEW);
		try {
			if (this.postInit != null) {
				this.postInit.accept(this.primaryScene);
			}
		} catch (Throwable var5) {
			var5.printStackTrace();
		}

		this.primaryStage.setTitle(title);
		this.primaryStage.show();
	}

	public final <T extends View> Optional<T> switchView(String viewName) {
		return this.switchView(viewName, ViewStackPolicy.USE);
	}

	public final <T extends View> Optional<T> switchView(String viewName, ViewStackPolicy viewStackPolicy) {
		if (this.getView() != null) {
			Event onCloseRequest = new View.LifecycleEvent(this.getView(), View.LifecycleEvent.CLOSE_REQUEST);
			Event.fireEvent(this.getView(), onCloseRequest);
			if (onCloseRequest.isConsumed()) {
				return Optional.empty();
			}
		}

		return this.doSwitchView(viewName, viewStackPolicy);
	}

	public final ReadOnlyObjectProperty<View> viewProperty() {
		return this.viewProperty.getReadOnlyProperty();
	}

	private void setView(View view) {
		this.viewProperty.set(view);
	}

	public final View getView() {
		return (View)this.viewProperty.get();
	}


	private <T extends View> Optional<T> doSwitchView(String viewName, ViewStackPolicy viewStackPolicy) {
		if (ViewStackPolicy.CLEAR == viewStackPolicy) {
			return this.viewFactory.get(viewName).map((newView) -> {
				this.viewStack.clear();
				this.setView(newView);
				return (T)newView;
			});
		} else {
			View currentView = this.getView();
			String currentViewName = ViewTools.findViewName(currentView);
			return currentView != null && currentViewName != null && currentViewName.equalsIgnoreCase(viewName) ? Optional.empty() : this.viewFactory.get(viewName).map((newView) -> {
				String newViewName = ViewTools.findViewName(newView);
				if (newViewName == null || newViewName.isEmpty()) {
					ViewTools.storeViewName(newView, viewName);
				}

				if (ViewStackPolicy.USE == viewStackPolicy && currentView != null) {
					this.viewStack.push(currentViewName);
				}

				if (currentView != null) {
					Event.fireEvent(currentView, new View.LifecycleEvent(currentView, View.LifecycleEvent.HIDING));
				}

				this.setView(newView);
				return (T)newView;
			});
		}
	}

}
