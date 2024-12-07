module galimimus.org.hlsclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.net.http;
    requires uk.co.caprica.vlcj.javafx;
    requires uk.co.caprica.vlcj;
    requires com.sun.jna;
    requires com.google.gson;
    requires java.logging;


    opens galimimus.org.hlsclient to javafx.fxml;
    exports galimimus.org.hlsclient;
    opens galimimus.org.hlsclient.controllers to javafx.fxml;
    exports galimimus.org.hlsclient.controllers;
    opens galimimus.org.hlsclient.models to com.google.gson;
}