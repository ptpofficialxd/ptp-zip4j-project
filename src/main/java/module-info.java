module ADV_Project {
    requires javafx.controls;
    requires javafx.fxml;
    requires zip4j;
    requires org.apache.commons.compress;


    opens se233.project to javafx.fxml;
    opens se233.project.controller to javafx.fxml;
    exports se233.project;
    exports se233.project.controller;
}