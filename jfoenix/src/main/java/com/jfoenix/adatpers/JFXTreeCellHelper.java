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
package com.jfoenix.adatpers;

import com.jfoenix.controls.JFXTreeView;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.scene.control.TreeItem;

public final class JFXTreeCellHelper {

    public static <T> void setTreeView(JFXTreeView<T> view, boolean newValue, int currentRow, TreeItem<T> item) {
        VirtualFlow<?> vf = (VirtualFlow<?>) view.lookup(".virtual-flow");
        if (!newValue) {
            int index = currentRow + item.getChildren().size() + 1;
            index = index > vf.getCellCount() ? vf.getCellCount() : index;
            view.height = (index - currentRow - 1) * vf.getCell(currentRow).getHeight();
        }
        view.layoutY = vf.getCell(currentRow).getLayoutY();
    }
}
