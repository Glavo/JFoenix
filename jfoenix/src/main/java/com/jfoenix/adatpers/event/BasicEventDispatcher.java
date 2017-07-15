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
package com.jfoenix.adatpers.event;

import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;

/**
 * Event dispatcher which introduces event dispatch phase specific methods -
 * {@code dispatchCapturingEvent} and {@code dispatchBubblingEvent}. These
 * are used in the {@code BasicEventDispatcher.dispatchEvent} implementation,
 * but because they are public they can be called directly as well. Their
 * default implementation does nothing and is expected to be overridden in
 * subclasses. The {@code BasicEventDispatcher} also adds possibility to chain
 * event dispatchers. This is used together with the direct access to the phase
 * specific dispatch methods to implement {@code CompositeEventDispatcher}.
 * <p>
 * An event dispatcher derived from {@code BasicEventDispatcher} can act as
 * a standalone event dispatcher or can be used to form a dispatch chain in
 * {@code CompositeEventDispatcher}.
 */
public abstract class BasicEventDispatcher implements EventDispatcher {
    private BasicEventDispatcher previousDispatcher;
    private BasicEventDispatcher nextDispatcher;

    @Override
    public Event dispatchEvent(Event event, final EventDispatchChain tail) {
        event = dispatchCapturingEvent(event);
        if (event.isConsumed()) {
            return null;
        }
        event = tail.dispatchEvent(event);
        if (event != null) {
            event = dispatchBubblingEvent(event);
            if (event.isConsumed()) {
                return null;
            }
        }

        return event;
    }

    public Event dispatchCapturingEvent(Event event) {
        return event;
    }

    public Event dispatchBubblingEvent(Event event) {
        return event;
    }

    public final BasicEventDispatcher getPreviousDispatcher() {
        return previousDispatcher;
    }

    public final BasicEventDispatcher getNextDispatcher() {
        return nextDispatcher;
    }

    public final void insertNextDispatcher(
        final BasicEventDispatcher newDispatcher) {
        if (nextDispatcher != null) {
            nextDispatcher.previousDispatcher = newDispatcher;
        }
        newDispatcher.nextDispatcher = nextDispatcher;
        newDispatcher.previousDispatcher = this;
        nextDispatcher = newDispatcher;
    }
}
