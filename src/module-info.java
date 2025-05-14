module gestion_notes {
    requires javafx.controls;
    requires javafx.fxml;

    opens controllers to javafx.fxml;
    opens application to javafx.graphics;


    exports controllers;
    
}
