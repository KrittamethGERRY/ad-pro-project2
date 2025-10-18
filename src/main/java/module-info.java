module se233.notcontra {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires org.apache.logging.log4j;
	requires javafx.base;
	requires javafx.media;

    opens se233.notcontra to javafx.fxml;
    exports se233.notcontra;
}