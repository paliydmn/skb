package com.palii.skb.controller;

import com.palii.skb.model.Tip;
import com.palii.skb.utils.CRUDHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class TipController {

    public Button fx_delete_item;
    @FXML
    private TextArea body_text;

    @FXML
    private Button copy_btn;
    @FXML
    private Button save_edit_btn;
    @FXML
    private Button expand_btn;

    @FXML
    private AnchorPane tip_item_id;

    @FXML
    private TextField title_text;

    @FXML
    private Label itemID;
    @FXML
    private Label editDateLbl;

    @FXML
    private Label useCountLbl;
    @FXML
    private Label createDateLbl;

    public void setItem(Tip tip) {
        body_text.setText(tip.getBody());
        title_text.setText(tip.getTitle());
        itemID.setText(String.valueOf(tip.getId()));
        useCountLbl.setText(String.valueOf(tip.getUseCount()));
        editDateLbl.setText(tip.getEditedDate());
        createDateLbl.setText(tip.getCreatedDate());
    }

    @FXML
    void initialize() {
        System.out.println("Tip:");
        System.out.println(this);
        body_text.textProperty().addListener((ob, o, n) -> {
            // TODO here
            System.out.println("Old " + o);
            System.out.println("New " + n);
            if (!o.isEmpty() && !o.equals(n)) {
                save_edit_btn.setDisable(false);
            }
            System.out.println(ob);
            Label l = (Label)save_edit_btn.getParent().getChildrenUnmodifiable().get(0);
            System.out.println(l.getText());
            System.out.println(itemID.getText());

            System.out.println("-----------------------------------");
        });

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

        String ID = itemID.getText();
        Tip t = getTips(ID).get(0);
        int useC = t.getUseCount() + 1;
        CRUDHelper.updateUsageCount(Integer.parseInt(ID), useC);
        int index = MainController.tvObservableList.indexOf(t);
        Tip tip = MainController.tvObservableList.get(index);
        tip.setUseCount(useC);
        MainController.tvObservableList.set(index, tip);
    }

    public void onDeleteItem(ActionEvent actionEvent) throws SQLException {
        System.out.println(actionEvent.getSource());
        String ID = itemID.getText();
        CRUDHelper.deleteItem(Integer.parseInt(ID));
        System.out.println(Integer.parseInt(ID));
        ObservableList<Tip> o = getTips(ID);
        MainController.tvObservableList.remove(o.get(0));
        MainController.setAllSearchStrArray();
    }

    private static ObservableList<Tip> getTips(String ID) {
        return MainController.tvObservableList.stream().filter(tip -> {
            return tip.getId().equals(Integer.parseInt(ID));
        }).collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public void onSaveChanges(ActionEvent actionEvent) {
        System.out.println("Saved");
        System.out.println(body_text.getText());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String editDate = sdf.format(timestamp);

        int ret = CRUDHelper.updateBody(body_text.getText(), Integer.parseInt(itemID.getText()), editDate);
        if (ret >= 1)
            save_edit_btn.setDisable(true);

        String ID = itemID.getText();
        Tip t = getTips(ID).get(0);
        int index = MainController.tvObservableList.indexOf(t);
        Tip tip = MainController.tvObservableList.get(index);
        tip.setEditedDate(editDate);
        tip.setBody(body_text.getText());
        MainController.tvObservableList.set(index, tip);
        //editDateLbl.setText(editDate);
    }

    public void onInputMethodChanged(InputMethodEvent inputMethodEvent) {

    }
}
