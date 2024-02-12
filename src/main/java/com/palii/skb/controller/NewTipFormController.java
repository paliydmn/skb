package com.palii.skb.controller;

import com.palii.skb.model.Tip;
import com.palii.skb.utils.CRUDHelper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NewTipFormController {
    @FXML
    public AnchorPane new_tip_root_Anchor;

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

    private Stage stage;
    private Scene scene;
    private Parent root;

    private ObservableList<Tip> appMainObservableList;

    @FXML
    void initialize() {
        System.out.println("Tip Controller! -> Init");
        cancelNewItemBtn.setOnAction(actionEvent -> {
            System.out.println("Cancel new tip is Clicked");
            closeStage(actionEvent);
        });
    }

    public void onAddNewItemBtn(ActionEvent actionEvent) {
        System.out.println("Add new tip is Clicked");
        String body = newBodyText.getText();
        String title = newTitleText.getText();
        appMainObservableList.add(new Tip(title, body, 0));
        CRUDHelper.addNewItem(title, body);
        MainController.setAllSearchStrArray();
        closeStage(actionEvent);
    }

    public void setAppMainObservableList(ObservableList<Tip> tvObservableList) {
        this.appMainObservableList = tvObservableList;
    }

    private void closeStage(ActionEvent event) {
        // CRUDHelper.addNewItem("a", newBodyText.getText() );
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
