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
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MainController {

    @FXML
    private Button createBtn;

    @FXML
    private ListView<Tip> mainListW;

    @FXML
    private TextField searchTF;
    @FXML
    private Pane addBtnPaneDrag;
    @FXML
    private ComboBox<String> searchByCmb;
    @FXML
    Label searchResultsLbl;
    @FXML
    private AnchorPane mainViewRootAnchor;
    public static ObservableList<Tip> tvObservableList = FXCollections.observableArrayList();
    DraggableMaker draggableMaker = new DraggableMaker();

    String searchBy = "byBody";

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        stickyAddBtn();
        keyCombHandling();
    }

    public static Collection<String> searchAutocompleteList;
    static AutocompletionTextField field;

    @FXML
    void initialize() {
        System.out.println(this);
        initSearchDropDown();
        //mainListW.setCellFactory();
        draggableMaker.makeDraggable(addBtnPaneDrag);

        mainListW.setCellFactory(mainListW -> {
            return new MyListCell();
        });
        mainListW.setItems(tvObservableList);
        refreshUI();


         field = new AutocompletionTextField(searchTF);
         setAllSearchStrArray();
       // field.getEntries().addAll(searchAutocompleteList);

        mainListW.getSelectionModel().selectedItemProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                System.out.println("ListView selection -> for tests ");
            }
        });
    }
//#ToDo rewrite this function!
    public static void setAllSearchStrArray() {
//        if (searchAutocompleteList != null && !searchAutocompleteList.isEmpty())
//            searchAutocompleteList.clear();
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
        field.getEntries().clear();
        field.getEntries().addAll(list);
     //   return list;
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
        searchByCmb.getItems().add("byBody");
        searchByCmb.getItems().add("byTitle");
        searchByCmb.getItems().add("byBody&Title");
        searchByCmb.getSelectionModel().selectFirst();
        searchByCmb.setOnAction((event) -> {
            int selectedIndex = searchByCmb.getSelectionModel().getSelectedIndex();
            Object selectedItem = searchByCmb.getSelectionModel().getSelectedItem();

            System.out.println("Selection made: [" + selectedIndex + "] " + selectedItem);
            System.out.println("ComboBox.getValue(): " + searchByCmb.getValue());
            searchBy = searchByCmb.getValue();
        });
    }

    public void addToList(String title, String body) {
        tvObservableList.add(new Tip(title, body, 0));
    }

    public void refreshUI() {
        ResultSet rs = CRUDHelper.readAllFromMain();
        tvObservableList.clear();
        while (true) {
            try {
                if (!rs.next()) break;
                String body = rs.getString("body");
                String title = rs.getString("title");
                int id = Integer.parseInt(rs.getString("id"));
                String editDate = rs.getString("edit_date");
                String createDate = rs.getString("_date");
                // assert rs.getString("use_count") != null;
                int useC = Integer.parseInt(rs.getString("use_count"));

                Tip e = new Tip(title, body, id, useC, editDate, createDate);
                //Tip e = new Tip(title, body, id);
                tvObservableList.add(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

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
        try (ResultSet rs = CRUDHelper.findAllLikeBy(searchTF.getText(), searchBy)) {
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
                    KeyCombination.CONTROL_ANY);

            public void handle(KeyEvent ke) {
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
