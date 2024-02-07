package com.example.knowledge_db;

import com.example.knowledge_db.controller.ItemController;
import com.example.knowledge_db.model.Tip;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MyListCell extends ListCell<Tip> {

    private AnchorPane root;
    private ItemController controller ;

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }

    public MyListCell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/knowledge_db/tip_element.fxml"));
            root = loader.load();
            ItemController tipItemController = loader.getController();
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

            //ItemController.setItem(item);
            setGraphic(root);
        }
    }
}
