/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.jfoenix.skins;

import com.jfoenix.adapters.ReflectionHelper;
import com.jfoenix.adapters.skins.DatePickerSkin;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.svg.SVGGlyph;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * <h1>Material Design Date Picker Skin</h1>
 *
 * @author Shadi Shaheen
 * @version 1.0
 * @since 2016-03-09
 */
public class JFXDatePickerSkin extends DatePickerSkin {

    /**
     * TODO:
     * 1. Handle different Chronology
     */
    private JFXDatePicker jfxDatePicker;

    // displayNode is the same as editorNode
    private TextField displayNode;
    private JFXDatePickerContent content;

    private JFXDialog dialog;

    public JFXDatePickerSkin(final JFXDatePicker datePicker) {
        super(datePicker);
        this.jfxDatePicker = datePicker;
        super.getPopupContent();
        try {
            Object expressionHelper = ReflectionHelper.getFieldContent(datePicker.focusedProperty().getClass().getSuperclass(), datePicker.focusedProperty(), "helper");
            ChangeListener[] changeListeners = ReflectionHelper.getFieldContent(expressionHelper, "changeListeners");
            // remove parent focus listener to prevent editor class cast exception
            datePicker.focusedProperty().removeListener(changeListeners[changeListeners.length - 1]);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        // add focus listener on editor node
        datePicker.focusedProperty().addListener((obj, oldVal, newVal) -> {
            if (getEditor() != null && !newVal) {
                ReflectionHelper.invoke(JFXDatePickerSkin.this, "setTextFromTextFieldIntoComboBoxValue");
            }
        });

        // create calender or clock button
        SVGGlyph glyph = new SVGGlyph(0,
            "calendar",
            "M320 384h128v128h-128zM512 384h128v128h-128zM704 384h128v128h-128zM128 "
            + "768h128v128h-128zM320 768h128v128h-128zM512 768h128v128h-128zM320 "
            + "576h128v128h-128zM512 576h128v128h-128zM704 576h128v128h-128zM128 "
            + "576h128v128h-128zM832 0v64h-128v-64h-448v64h-128v-64h-128v1024h960v-1024h-128zM896"
            + " 960h-832v-704h832v704z",
            Color.BLACK);
        setArrow(glyph);
        glyph.fillProperty().bind(jfxDatePicker.defaultColorProperty());
        glyph.setSize(20, 20);
        getArrowButton().getChildren().setAll(glyph);
        getArrowButton().setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,
            CornerRadii.EMPTY,
            Insets.EMPTY)));
        getArrowButton().setPadding(new Insets(1,8,1,8));

        registerChangeListener2(datePicker.converterProperty(), "CONVERTER", () -> {
            updateDisplayNode2();
        });
        registerChangeListener2(datePicker.dayCellFactoryProperty(), "DAY_CELL_FACTORY", () -> {
            updateDisplayNode2();
            content = null;
            setPopup2(null);
        });
        registerChangeListener2(datePicker.editorProperty(), "EDITOR", () -> {
            getEditableInputNode2();
        });
        registerChangeListener2(datePicker.showingProperty(), "SHOWING", () -> {
            if (jfxDatePicker.isShowing()) {
                if (content != null) {
                    LocalDate date = jfxDatePicker.getValue();
                    // set the current date / now when showing the date picker content
                    content.displayedYearMonthProperty().set((date != null) ?
                        YearMonth.from(date) : YearMonth.now());
                    content.updateValues();
                }
                show();
            } else {
                hide();
            }
        });
        registerChangeListener2(datePicker.showWeekNumbersProperty(), "SHOW_WEEK_NUMBERS", () -> {
            if (content != null) {
                // update the content grid to show week numbers
                content.updateContentGrid();
                content.updateWeekNumberDateCells();
            }
        });
        registerChangeListener2(datePicker.valueProperty(), "VALUE", () -> {
            updateDisplayNode2();
            if (content != null) {
                LocalDate date = jfxDatePicker.getValue();
                content.displayedYearMonthProperty().set((date != null) ?
                    YearMonth.from(date) : YearMonth.now());
                content.updateValues();
            }
            jfxDatePicker.fireEvent(new ActionEvent());
        });
    }

    @Override
    public Node getPopupContent() {
        if (content == null) {
            // different chronologies are not supported yet
            content = new JFXDatePickerContent(jfxDatePicker);
        }
        return content;
    }

    @Override
    public void show() {
        if (!((JFXDatePicker) getSkinnable()).isOverLay()) {
            super.show();
        }
        if (content != null) {
            content.init();
            content.clearFocus();
        }
        if (((JFXDatePicker) getSkinnable()).isOverLay()) {
            if (dialog == null) {
                StackPane dialogParent = jfxDatePicker.getDialogParent();
                if (dialogParent == null) {
                    dialogParent = (StackPane) getSkinnable().getScene().getRoot();
                }
                dialog = new JFXDialog(dialogParent, (Region) getPopupContent(), DialogTransition.CENTER, true);
                getArrowButton().setOnMouseClicked((click) -> {
                    if (((JFXDatePicker) getSkinnable()).isOverLay()) {
                        StackPane parent = jfxDatePicker.getDialogParent();
                        if (parent == null) {
                            parent = (StackPane) getSkinnable().getScene().getRoot();
                        }
                        dialog.show(parent);
                    }
                });
            }
        }
    }

    @Override
    protected TextField getEditor() {
        return ((DatePicker) getSkinnable()).getEditor();
    }

    @Override
    protected StringConverter<LocalDate> getConverter() {
        return ((DatePicker) getSkinnable()).getConverter();
    }

    @Override
    public Node getDisplayNode() {
        if (displayNode == null) {
            displayNode = getEditableInputNode2();
            displayNode.getStyleClass().add("date-picker-display-node");
            updateDisplayNode2();
        }
        displayNode.setEditable(jfxDatePicker.isEditable());
        return displayNode;
    }

    /*
     * this method is called from the behavior class to make sure
     * DatePicker button is in sync after the popup is being dismissed
     */
    public void syncWithAutoUpdate() {
        if (!getPopup2().isShowing() && jfxDatePicker.isShowing()) {
            jfxDatePicker.hide();
        }
    }
}

