module se233.notcontra {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires org.apache.logging.log4j;
	
    opens se233.notcontra to javafx.fxml;
    exports se233.notcontra;
}