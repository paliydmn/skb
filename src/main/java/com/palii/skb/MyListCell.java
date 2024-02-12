package com.palii.skb;

import com.palii.skb.controller.TipController;
import com.palii.skb.model.Tip;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MyListCell extends ListCell<Tip> {

    private final AnchorPane root;
    private final TipController controller ;

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }

    public MyListCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/palii/skb/tip_element.fxml"));
            root = loader.load();
            controller = loader.getController();
        } catch (IOException exc) {
            // this is basically fatal, so just bail here:
            throw new RuntimeException(exc);
        }
    }
    @Override
    protected void updateItem(Tip item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            controller.setItem(item);
            setGraphic(root);
        }
    }
}
