module gestion_notes {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;

    opens application to javafx.graphics, javafx.fxml;
    opens controllers to javafx.fxml;
    opens models to javafx.base;
    opens views to javafx.fxml;

    exports application;
    exports controllers;
    exports models;
}