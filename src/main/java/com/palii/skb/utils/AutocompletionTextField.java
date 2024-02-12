package com.palii.skb.utils;

import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class AutocompletionTextField {
    //Local variables
    //entries to autocomplete
    private final TextField searchTF;
    private final SortedSet<String> entries;
    //popup GUI
    private final ContextMenu entriesPopup;
    private List<String> searchResult;
    private String searchRequest;


    public AutocompletionTextField(TextField searchTF) {
        super();
        this.entries = new TreeSet<>();
        this.entriesPopup = new ContextMenu();
        this.searchTF = searchTF;
        setListener();
    }

    private void setListener() {
        //Add "suggestions" by changing text
        searchTF.textProperty().addListener((observable, oldValue, newValue) -> {
            String enteredText = searchTF.getText();
            //always hide suggestion if nothing has been entered (only "spacebars" are disallowed in TextField)
            if (enteredText == null || enteredText.isEmpty()) {
                entriesPopup.hide();
            } else {
                //filter all possible suggestions depends on "Text", case insensitive
                List<String> filteredEntries = entries.stream()
                        .filter(e -> e.toLowerCase().contains(enteredText.toLowerCase()))
                        .collect(Collectors.toList());
                //some suggestions are found
                if (!filteredEntries.isEmpty()) {
                    //build popup - list of "CustomMenuItem"
                    populatePopup(filteredEntries, enteredText);
                    if (!entriesPopup.isShowing()) { //optional
                        entriesPopup.show(searchTF, Side.BOTTOM, 0, 0); //position of popup
                    }
                //no suggestions -> hide
                } else {
                    entriesPopup.hide();
                }
            }
        });

        //Hide always by focus-in (optional) and out
        searchTF.focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            entriesPopup.hide();
        });
    }
    private void populatePopup(List<String> searchResult, String searchRequest) {
        //List of "suggestions"
        List<CustomMenuItem> menuItems = new LinkedList<>();
        //List size - 10 or founded suggestions count
        int maxEntries = 10;
        int count = Math.min(searchResult.size(), maxEntries);
        //Build list as set of labels
        for (int i = 0; i < count; i++) {
          final String result = searchResult.get(i);
          //label with graphic (text flow) to highlight founded subtext in suggestions
          Label entryLabel = new Label();
          entryLabel.setGraphic(Styles.buildTextFlow(result, searchRequest));
          entryLabel.setPrefHeight(10);  //don't sure why it's changed with "graphic"
          CustomMenuItem item = new CustomMenuItem(entryLabel, true);
          menuItems.add(item);
          //if any suggestion is select set it into text and close popup
          item.setOnAction(actionEvent -> {
              searchTF.setText(result);
              searchTF.positionCaret(result.length());
              entriesPopup.hide();
          });
        }

        //"Refresh" context menu
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }

    public SortedSet<String> getEntries() { return entries; }
}