package com.example.knowledge_db.controller;

import com.example.knowledge_db.utils.CRUDHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Popup;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NewTipFormController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addNewItemBtn;

    @FXML
    private Button cancelNewItemBtn;

    @FXML
    private TextArea newBodyText;

    @FXML
    private TextField newTitleText;

    @FXML
    void initialize() {
        addNewItemBtn.setOnAction(actionEvent -> {
            Popup stage = (Popup) ((Node) actionEvent.getSource()).getScene().getWindow();
            try {
                CRUDHelper.addNewItem(newTitleText.getText(), newBodyText.getText());
             //   MainController.observableList.add(newBodyText.getParent());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            stage.hide();
        });

        cancelNewItemBtn.setOnAction(actionEvent -> {
            Popup stage = (Popup) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.hide();
        });
    }

    public void onAddNewItemBtn(ActionEvent actionEvent) {
    }
}
