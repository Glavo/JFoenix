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

import javafx.beans.value.ObservableValue;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.PopupControl;
import javafx.scene.layout.StackPane;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ColorPickerSkin extends com.sun.javafx.scene.control.skin.ColorPickerSkin {
    public ColorPickerSkin(ColorPicker colorPicker) {
        super(colorPicker);
    }

    protected void registerChangeListener2(ObservableValue<?> property, String key, Runnable listener) {
        registerChangeListener(property, key);
        changeListeners.put(key, listener);
    }

    private Map<String, Runnable> changeListeners = new HashMap<>();

    @Override
    protected void handleControlPropertyChanged(String propertyReference) {
        if (changeListeners.containsKey(propertyReference)) {
            changeListeners.get(propertyReference).run();
        }
        super.handleControlPropertyChanged(propertyReference);
    }

    protected StackPane getArrowButton() {
        return arrowButton;
    }

    protected PopupControl getPopup2() {
        return getPopup();
    }
}
