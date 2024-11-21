module com.josh.avi_lite {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
requires org.json;
requires okhttp3;

             opens controller to javafx.fxml;
            exports controller;
            opens com.josh.avi_lite to javafx.fxml;
            exports com.josh.avi_lite;
            
             opens utils to javafx.base;
            
            
            
}
