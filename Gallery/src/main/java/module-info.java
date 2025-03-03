module com.example.gallary {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gallary to javafx.fxml;
    exports com.example.gallary;
}