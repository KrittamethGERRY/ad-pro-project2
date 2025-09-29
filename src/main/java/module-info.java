module se233.notcontra {
    requires javafx.controls;
    requires javafx.fxml;


    opens se233.notcontra to javafx.fxml;
    exports se233.notcontra;
}