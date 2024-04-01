package com.palii.skb.controller;

import com.palii.skb.MyListCell;
import com.palii.skb.model.Tip;
import com.palii.skb.utils.AutocompletionTextField;
import com.palii.skb.utils.CRUDHelper;
import com.palii.skb.utils.DraggableMaker;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MainController {

    @FXML
    private ListView<Tip> mainListW;
    @FXML
    private ListView<Tip> mostUsedListView;
    @FXML
    private TextField searchTF;
    @FXML
    private Pane addBtnPaneDrag;
    @FXML
    private ComboBox<String> searchByCmb;
    @FXML
    Label searchResultsLbl;
    @FXML
    Label tipsInDbLabel;
    @FXML
    public static ObservableList<Tip> tvObservableList = FXCollections.observableArrayList();
    @FXML
    public static ObservableList<Tip> tvMostUsedObsList = FXCollections.observableArrayList();
    DraggableMaker draggableMaker = new DraggableMaker();
    private Stage stage;
    static AutocompletionTextField field;

    public void setStage(Stage stage) {
        this.stage = stage;
        stickyAddBtn();
        keyCombHandling();
    }

    @FXML
    void initialize() {
        System.out.println(this);

        draggableMaker.makeDraggable(addBtnPaneDrag);

        mainListW.setCellFactory(mainListW -> new MyListCell("tip_element.fxml"));
        mostUsedListView.setCellFactory(mainListW -> new MyListCell("tip_element_short.fxml"));
        mainListW.setItems(tvObservableList);
        mostUsedListView.setItems(tvMostUsedObsList);
        refreshUI();
        refreshMostUsed();

         field = new AutocompletionTextField(searchTF);
         setAllSearchStrArray();
        mainListW.getSelectionModel().selectedItemProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                System.out.println("ListView selection -> for tests ");
            }
        });
        initSearchDropDown();
    }
//#ToDo rewrite this function!
    public static void setAllSearchStrArray() {
        ArrayList<String> list = new ArrayList<String>();
        ResultSet rs = CRUDHelper.findAllInDb();
        while (true) {
            try {
                if (!rs.next()) break;
                String body = rs.getString("body");
                String[] lines = Arrays.stream(body.split("\n")).filter(str -> !str.isEmpty()).toArray(String[]::new);
                list.addAll(List.of(lines));
                list.add(rs.getString("title"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        field.getEntries().clear();
        field.getEntries().addAll(list);
    }

    private void stickyAddBtn() {
        // Stage stage = (Stage) addBtnPaneDrag.getScene().getWindow();

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            //    System.out.println("Height: " + stage.getHeight() + " Width: " + stage.getWidth());
            addBtnPaneDrag.setLayoutX(stage.getWidth() - 90);
            addBtnPaneDrag.setLayoutY(stage.getHeight() - 100);
        };

        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);
    }

    private void initSearchDropDown() {
        searchByCmb.getItems().add("byBody&Title");
        searchByCmb.getItems().add("byBody");
        searchByCmb.getItems().add("byTitle");
        searchByCmb.getSelectionModel().selectFirst();
        searchByCmb.setOnAction((event) -> {
            int selectedIndex = searchByCmb.getSelectionModel().getSelectedIndex();
            Object selectedItem = searchByCmb.getSelectionModel().getSelectedItem();
            System.out.println("Selection made: [" + selectedIndex + "] " + selectedItem);
            System.out.println("ComboBox.getValue(): " + searchByCmb.getValue());
        });

    }

    public void refreshUI() {
        ResultSet rs = CRUDHelper.readAllFromMain();
        tvObservableList.clear();
        while (true) {
            try {
                if (!rs.next()) break;
                int id = Integer.parseInt(rs.getString("id"));
                String body = rs.getString("body");
                String title = rs.getString("title");
                int useC = Integer.parseInt(rs.getString("use_count"));
                String editDate = rs.getString("edit_date");
                String createDate = rs.getString("_date");
                tvObservableList.add(new Tip(title, body, id, useC, editDate, createDate));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tipsInDbLabel.setText(String.valueOf(tvObservableList.size()));
        refreshMostUsed();
    }

    public static void refreshMostUsed() {
        tvMostUsedObsList.clear();
        tvMostUsedObsList.addAll(tvObservableList);
        tvMostUsedObsList.sort(new Comparator<Tip>() {
            @Override
            public int compare(Tip o1, Tip o2) {
                int c = o1.getUseCount();
                int c1 = o2.getUseCount();
                return Integer.compare(c1, c);
            }
        });
    }

    @FXML
    void onCreateBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/palii/skb/new_tip_form.fxml"));
        Parent parent = fxmlLoader.load();
        NewTipFormController dialogController = fxmlLoader.getController();
        dialogController.setAppMainObservableList(tvObservableList);

        Scene scene = new Scene(parent, 500, 200);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        //on close if needed
        stage.setOnHiding(e -> {
            System.out.println("Closing Stage");
            refreshUI();
        });
        stage.showAndWait();
    }

    @FXML
    void onSearch(ActionEvent event) {
        int c = 0;
        try (ResultSet rs = CRUDHelper.findAllLikeBy(searchTF.getText(), searchByCmb.getValue())) {
            tvObservableList.clear();
            while (true) {
                try {
                    if (!rs.next()) break;
                    String title = rs.getString("title");
                    String body = rs.getString("body");
                    int id = Integer.parseInt(rs.getString("id"));
                    int useC = Integer.parseInt(rs.getString("use_count"));
                    String editDate = rs.getString("edit_date");
                    String createDate = rs.getString("_date");
                    Tip e = new Tip(title, body, id, useC, editDate, createDate);
                    tvObservableList.add(e);
                    c++;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        searchResultsLbl.setText("Found: " + c);
        stickyAddBtn();
    }

    private void keyCombHandling() {
        mainListW.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyCombAddNew = new KeyCodeCombination(KeyCode.N,
                    KeyCombination.CONTROL_DOWN);

            public void handle(KeyEvent ke) {
               // if (KeyCode.N.equals(ke.getCode()) && ke.isControlDown()) {
                if (keyCombAddNew.match(ke)) {
                    System.out.println("Key Pressed: " + keyCombAddNew);
                    try {
                        onCreateBtnClick(new ActionEvent());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    ke.consume(); // <-- stops passing the event to next node
                }
            }
        });
    }
}
