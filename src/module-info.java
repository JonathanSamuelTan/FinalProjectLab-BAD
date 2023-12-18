module FinalProjectLabBAD {
    requires javafx.controls;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.swt;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.fxml;
    requires java.sql;
//  kalau error, ganti namanya sesuai dengan nama jar nya :D  
    requires mysql.connector.j;
    
    opens main;
    opens controller;
    opens model;
    opens view;

}
