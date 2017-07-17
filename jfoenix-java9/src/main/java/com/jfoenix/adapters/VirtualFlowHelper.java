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

import javafx.scene.control.IndexedCell;
import javafx.scene.control.TreeView;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.control.TreeItem;

import java.util.function.BiFunction;

public final class VirtualFlowHelper {

    public static <T> void setTreeView(TreeView<T> view, boolean newValue, int currentRow, TreeItem<T> item) {
        VirtualFlow<?> vf = (VirtualFlow<?>) view.lookup(".virtual-flow");
        IJFXTreeView view2 = (IJFXTreeView) view;
        if (!newValue) {
            int index = currentRow + item.getChildren().size() + 1;
            index = index > vf.getCellCount() ? vf.getCellCount() : index;
            view2.setHeight2((index - currentRow - 1) * vf.getCell(currentRow).getHeight());
        }
        view2.setLayoutY2(vf.getCell(currentRow).getLayoutY());
    }

    public static <T extends IndexedCell, R extends Number> R forEach(Object virtualFlow, R init, BiFunction<T, R, R> func) {
        VirtualFlow<T> flow = (VirtualFlow<T>) virtualFlow;

        for (int i = 0; i < flow.getCellCount(); i++) {
            T cell = flow.getCell(i);
            init = func.apply(cell, init);
        }
        return init;
    }
}
