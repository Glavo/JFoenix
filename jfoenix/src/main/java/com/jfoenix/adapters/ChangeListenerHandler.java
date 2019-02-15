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
package com.jfoenix.adapters;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class ChangeListenerHandler {

    private final Map<ObservableValue<?>, Consumer<ObservableValue<?>>> propertyReferenceMap;
    private final ChangeListener<Object> propertyChangedListener;
    private final WeakChangeListener<Object> weakPropertyChangedListener;

    private static final Consumer<ObservableValue<?>> EMPTY_CONSUMER = e -> {};

    public ChangeListenerHandler() {
        this.propertyReferenceMap = new HashMap<>();
        this.propertyChangedListener = (observable, oldValue, newValue) -> {
            propertyReferenceMap.getOrDefault(observable, EMPTY_CONSUMER).accept(observable);
        };
        this.weakPropertyChangedListener = new WeakChangeListener<>(propertyChangedListener);
    }

    public final void registerChangeListener(ObservableValue<?> property, Consumer<ObservableValue<?>> consumer) {
        if (consumer == null) return;

        if (!propertyReferenceMap.containsKey(property)) {
            property.addListener(weakPropertyChangedListener);
        }

        propertyReferenceMap.merge(property, consumer, Consumer::andThen);
    }

    public final Consumer<ObservableValue<?>> unregisterChangeListeners(ObservableValue<?> property) {
        property.removeListener(weakPropertyChangedListener);
        return propertyReferenceMap.remove(property);
    }

    public void dispose() {
        for (ObservableValue<?> value : propertyReferenceMap.keySet()) {
            value.removeListener(weakPropertyChangedListener);
        }
        propertyReferenceMap.clear();
    }
}
