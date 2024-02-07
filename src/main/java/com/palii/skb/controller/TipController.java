package com.palii.skb.controller;

import com.palii.skb.model.Tip;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;

public class ItemController {

    public Button fx_delete_item;
    @FXML
    private TextArea body_text;

    @FXML
    private Button copy_btn;

    @FXML
    private Button expand_btn;

    @FXML
    private AnchorPane fx_tip_item_id;

    @FXML
    private TextField title_text;

    @FXML
    private Label fx_itemID;


    public void setItem(Tip item) {
        body_text.setText(item.getText());
        title_text.setText(item.getText());
    }
    @FXML
    void initialize() {

//        body_text.setText("RRRRRRRR");
    }

    public void onExpandItem(ActionEvent actionEvent) {
        System.out.println(body_text.getPrefHeight());

        //   body_text.setPrefHeight(Region.USE_COMPUTED_SIZE);
        if (body_text.getPrefHeight() == 75) {
            body_text.setPrefHeight((body_text.getParagraphs().size() + 6) * body_text.getFont().getSize());

        } else {
            body_text.setPrefHeight(75);
        }
    }

    public void onCopyItem(ActionEvent actionEvent) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(body_text.getText());
        clipboard.setContent(content);
    }

    public void onDeleteItem(ActionEvent actionEvent) throws SQLException {

        System.out.println(actionEvent.getSource());
        //CRUDHelper.deleteItem(Integer.parseInt(fx_itemID.getText()));
        //MainController.observableList.remove(body_text.getParent());
        //MainController.obsIntTest.remove(1);
    }
}
