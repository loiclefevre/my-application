module com.mycompany.sample {
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires com.gluonhq.charm.glisten;
	requires com.gluonhq.attach.lifecycle;
	requires com.gluonhq.attach.util;
	requires com.gluonhq.attach.display;
	requires com.gluonhq.attach.storage;

	exports com.mycompany.sample;
	opens com.mycompany.sample to javafx.fxml, com.gluonhq.attach.display, com.gluonhq.attach.storage;

}