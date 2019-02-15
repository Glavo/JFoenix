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
package com.jfoenix.adapters.skins;

import com.jfoenix.adapters.ReflectionHelper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ComboBoxPopupControl;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.lang.reflect.Field;

public class DatePickerSkin extends javafx.scene.control.skin.DatePickerSkin {
    public DatePickerSkin(DatePicker datePicker) {
        super(datePicker);
    }

    protected void registerChangeListener2(ObservableValue<?> property, String key, Runnable listener) {
        registerChangeListener(property, (property2) -> listener.run());
    }

    private final Field arrowButtonField = ReflectionHelper.getField(javafx.scene.control.skin.ComboBoxBaseSkin.class,"arrowButton");

    protected StackPane getArrowButton() {
        try {
            return (StackPane) arrowButtonField.get(this);
        } catch (NullPointerException | IllegalAccessException e) {
            throw new IllegalAccessError("Cannot access arrowButton, this should not happen.");
        }
    }

    private final Field popupField = ReflectionHelper.getField(javafx.scene.control.skin.ComboBoxPopupControl.class,"popup");

    protected PopupControl getPopup2() {
        return ReflectionHelper.invoke(ComboBoxPopupControl.class, this, "getPopup");
    }

    protected void setPopup2(PopupControl popup) {
        try {
            popupField.set(this, popup);
        } catch (NullPointerException | IllegalAccessException e) {
            throw new IllegalAccessError("Cannot access arrow, this should not happen.");
        }
    }

    private final Field arrowField = ReflectionHelper.getField(javafx.scene.control.skin.ComboBoxBaseSkin.class,"arrow");

    protected Region getArrow() {
        try {
            return (Region) arrowField.get(this);
        } catch (NullPointerException | IllegalAccessException e) {
            throw new IllegalAccessError("Cannot access arrow, this should not happen.");
        }
    }

    protected void setArrow(Region arrow) {
        try {
            arrowField.set(this, arrow);
        } catch (NullPointerException | IllegalAccessException e) {
            throw new IllegalAccessError("Cannot access arrow, this should not happen.");
        }
    }

    protected void updateDisplayNode2() {
        ReflectionHelper.invoke(ComboBoxPopupControl.class, this, "updateDisplayNode");
    }

    protected TextField getEditableInputNode2() {
        return ReflectionHelper.invoke(ComboBoxPopupControl.class, this, "getEditableInputNode");
    }

    protected void setTextFromTextFieldIntoComboBoxValue2() {
        ReflectionHelper.invoke(ComboBoxPopupControl.class, this, "setTextFromTextFieldIntoComboBoxValue");
    }
}
