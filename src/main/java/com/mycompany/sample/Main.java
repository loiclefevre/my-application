package com.mycompany.sample;

import com.mycompany.sample.javafx.ApplicationManager;
import javafx.application.Application;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static com.mycompany.sample.javafx.ApplicationManager.HOME_VIEW;

public class Main extends Application {

	public static final String MAIN_VIEW = HOME_VIEW;

	private final ApplicationManager applicationManager = ApplicationManager.initialize("My Application", this::postInit);

	@Override
	public void init() {
		applicationManager.addViewFactory(MAIN_VIEW, () -> new MainView().getView());
	}

	@Override
	public void start(Stage stage) {
		applicationManager.start(stage);
	}

	private void postInit(Scene scene) {
		scene.getStylesheets().add(Main.class.getResource("styles.css").toExternalForm());
		((Stage) scene.getWindow()).getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));

		Dimension2D dimension2D = new Dimension2D(640, 480);
		scene.getWindow().setWidth(dimension2D.getWidth());
		scene.getWindow().setHeight(dimension2D.getHeight());
	}

	public static void main(String[] args) {
		launch(args);
	}
}
