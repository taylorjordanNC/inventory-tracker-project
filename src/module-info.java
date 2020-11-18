module C482ProjectJavaFX {
    requires javafx.controls;
    requires javafx.fxml;

    opens controller;
    opens main;
    opens model;
    opens view;
}