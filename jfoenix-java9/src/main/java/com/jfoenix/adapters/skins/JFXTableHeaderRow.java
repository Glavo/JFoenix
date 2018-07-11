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

import javafx.scene.control.TableColumnBase;

/**
 * @author Shadi Shaheen
 */
public class JFXTableHeaderRow {

    public JFXTableHeaderRow(final Object skin2, final TableColumnBase base) {

        row = new TableHeaderRow(skin2) {
            @Override
            protected NestedTableColumnHeader createRootHeader() {
                return new JFXNestedTableColumnHeader(null, this, base);
            }
        };
    }

    TableHeaderRow row;

    public TableHeaderRow getRow() {
        return row;
    }
}