package com.example.knowledge_db.controller;

import com.example.knowledge_db.model.Tip;
import com.example.knowledge_db.utils.SQLiteDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Button createBtn;

    @FXML
    private ListView<Tip> mainListW;

    @FXML
    private TextField searchTF;

    @FXML
    void onCreateBtnClick(ActionEvent event) {

    }

    @FXML
    void onSearch(ActionEvent event) {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SQLiteDB.isOK();

    }


}