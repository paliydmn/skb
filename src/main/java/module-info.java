module com.example.knowledge_db {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.xerial.sqlitejdbc;

    opens com.palii.skb to javafx.fxml;
    exports com.palii.skb;
    exports com.palii.skb.controller;
    opens com.palii.skb.controller to javafx.fxml;
    exports com.palii.skb.model;
    opens com.palii.skb.model to javafx.fxml;
}