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
import javafx.animation.Animation;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.skin.ProgressIndicatorSkin;

import java.lang.reflect.Field;

public class ProgressBarSkin extends javafx.scene.control.skin.ProgressBarSkin {
    public ProgressBarSkin(ProgressBar control) {
        super(control);
    }

    protected void registerChangeListener2(ObservableValue<?> property, String key, Runnable listener) {
        registerChangeListener(property, (property2) -> listener.run());
    }

    private final Field indeterminateTransitionField = ReflectionHelper.getField(ProgressIndicatorSkin.class, "indeterminateTransition");

    protected Animation getIndeterminateTransition() {
        try {
            return (Animation) indeterminateTransitionField.get(this);
        } catch (NullPointerException | IllegalAccessException e) {
            throw new IllegalAccessError("Cannot access indeterminateTransition, this should not happen.");
        }
    }

    protected void setIndeterminateTransition(Animation animation) {
        try {
            indeterminateTransitionField.set(this, animation);
        } catch (NullPointerException | IllegalAccessException e) {
            throw new IllegalAccessError("Cannot access indeterminateTransition, this should not happen.");
        }
    }
}
