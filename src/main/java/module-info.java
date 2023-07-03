module acamo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires jsonstream;
    requires json;
    requires leafletmap;


    opens acamo to javafx.fxml;
    exports acamo;
}
