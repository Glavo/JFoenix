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

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Paint;

public class TextAreaSkin extends javafx.scene.control.skin.TextAreaSkin {
    public TextAreaSkin(TextArea textArea) {
        super(textArea);
    }

    protected final ObjectProperty<Paint> promptTextFillProperty2() {
        return promptTextFillProperty();
    }

    protected final Paint getPromptTextFill2() {
        return getPromptTextFill();
    }

    protected final void setPromptTextFill2(Paint value) {
        setPromptTextFill(value);
    }

    protected void registerChangeListener2(ObservableValue<?> property, String key, Runnable listener) {
        registerChangeListener(property, (property2) -> listener.run());
    }
}
