module com.mycompany.sample {
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires com.gluonhq.attach.util;
	requires com.gluonhq.attach.display;
	requires com.gluonhq.attach.storage;

	exports com.mycompany.sample to javafx.fxml, javafx.graphics;
	exports com.mycompany.sample.javafx to javafx.fxml, javafx.graphics;
	opens com.mycompany.sample to javafx.fxml, com.gluonhq.attach.display, com.gluonhq.attach.storage;
}