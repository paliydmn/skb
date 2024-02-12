package com.palii.skb;

import com.palii.skb.controller.MainController;
import com.palii.skb.utils.DraggableMaker;
import com.palii.skb.utils.SQLiteDB;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    public static void main(String[] args) {
        launch();

    }

    @Override
    public void start(Stage stage) throws IOException {
        //create db and table if not exists
        SQLiteDB.isOK();
        SQLiteDB.createNewDatabase();
        SQLiteDB.createNewTable();


        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setTitle("Simple Knowledge DB!");
        stage.setScene(scene);
        stage.show();
        // set stage to observe window resize. make draggable Add button sticky
        ((MainController) fxmlLoader.getController()).setStage(stage);
    }

}