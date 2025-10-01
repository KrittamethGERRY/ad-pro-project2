module se233.notcontra {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;

    opens se233.notcontra to javafx.fxml;
    exports se233.notcontra;
}