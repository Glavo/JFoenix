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
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;

public final class CompositeEventHandler<T extends Event> {
    private EventProcessorRecord<T> firstRecord;
    private EventProcessorRecord<T> lastRecord;

    private EventHandler<? super T> eventHandler;

    public void setEventHandler(final EventHandler<? super T> eventHandler) {
        this.eventHandler = eventHandler;
    }

    public EventHandler<? super T> getEventHandler() {
        return eventHandler;
    }

    public void addEventHandler(final EventHandler<? super T> eventHandler) {
        if (find(eventHandler, false) == null) {
            append(lastRecord, createEventHandlerRecord(eventHandler));
        }
    }

    public void removeEventHandler(final EventHandler<? super T> eventHandler) {
        final EventProcessorRecord<T> record = find(eventHandler, false);
        if (record != null) {
            remove(record);
        }
    }

    public void addEventFilter(final EventHandler<? super T> eventFilter) {
        if (find(eventFilter, true) == null) {
            append(lastRecord, createEventFilterRecord(eventFilter));
        }
    }

    public void removeEventFilter(final EventHandler<? super T> eventFilter) {
        final EventProcessorRecord<T> record = find(eventFilter, true);
        if (record != null) {
            remove(record);
        }
    }

    public void dispatchBubblingEvent(final Event event) {
        final T specificEvent = (T) event;

        EventProcessorRecord<T> record = firstRecord;
        while (record != null) {
            if (record.isDisconnected()) {
                remove(record);
            } else {
                record.handleBubblingEvent(specificEvent);
            }
            record = record.nextRecord;
        }

        if (eventHandler != null) {
            eventHandler.handle(specificEvent);
        }
    }

    public void dispatchCapturingEvent(final Event event) {
        final T specificEvent = (T) event;

        EventProcessorRecord<T> record = firstRecord;
        while (record != null) {
            if (record.isDisconnected()) {
                remove(record);
            } else {
                record.handleCapturingEvent(specificEvent);
            }
            record = record.nextRecord;
        }
    }

    /* Used for testing. */
    boolean containsHandler(final EventHandler<? super T> eventHandler) {
        return find(eventHandler, false) != null;
    }

    /* Used for testing. */
    boolean containsFilter(final EventHandler<? super T> eventFilter) {
        return find(eventFilter, true) != null;
    }

    private EventProcessorRecord<T> createEventHandlerRecord(
        final EventHandler<? super T> eventHandler) {
        return (eventHandler instanceof WeakEventHandler)
            ? new WeakEventHandlerRecord(
            (WeakEventHandler<? super T>) eventHandler)
            : new NormalEventHandlerRecord(eventHandler);
    }

    private EventProcessorRecord<T> createEventFilterRecord(
        final EventHandler<? super T> eventFilter) {
        return (eventFilter instanceof WeakEventHandler)
            ? new WeakEventFilterRecord(
            (WeakEventHandler<? super T>) eventFilter)
            : new NormalEventFilterRecord(eventFilter);
    }

    private void remove(final EventProcessorRecord<T> record) {
        final EventProcessorRecord<T> prevRecord = record.prevRecord;
        final EventProcessorRecord<T> nextRecord = record.nextRecord;

        if (prevRecord != null) {
            prevRecord.nextRecord = nextRecord;
        } else {
            firstRecord = nextRecord;
        }

        if (nextRecord != null) {
            nextRecord.prevRecord = prevRecord;
        } else {
            lastRecord = prevRecord;
        }

        // leave record.nextRecord set
    }

    private void append(final EventProcessorRecord<T> prevRecord,
                        final EventProcessorRecord<T> newRecord) {
        EventProcessorRecord<T> nextRecord;
        if (prevRecord != null) {
            nextRecord = prevRecord.nextRecord;
            prevRecord.nextRecord = newRecord;
        } else {
            nextRecord = firstRecord;
            firstRecord = newRecord;
        }

        if (nextRecord != null) {
            nextRecord.prevRecord = newRecord;
        } else {
            lastRecord = newRecord;
        }

        newRecord.prevRecord = prevRecord;
        newRecord.nextRecord = nextRecord;
    }

    private EventProcessorRecord<T> find(
        final EventHandler<? super T> eventProcessor,
        final boolean isFilter) {
        EventProcessorRecord<T> record = firstRecord;
        while (record != null) {
            if (record.isDisconnected()) {
                remove(record);
            } else if (record.stores(eventProcessor, isFilter)) {
                return record;
            }

            record = record.nextRecord;
        }

        return null;
    }

    private static abstract class EventProcessorRecord<T extends Event> {
        private EventProcessorRecord<T> nextRecord;
        private EventProcessorRecord<T> prevRecord;

        public abstract boolean stores(EventHandler<? super T> eventProcessor,
                                       boolean isFilter);

        public abstract void handleBubblingEvent(T event);

        public abstract void handleCapturingEvent(T event);

        public abstract boolean isDisconnected();
    }

    private static final class NormalEventHandlerRecord<T extends Event>
        extends EventProcessorRecord<T> {
        private final EventHandler<? super T> eventHandler;

        public NormalEventHandlerRecord(
            final EventHandler<? super T> eventHandler) {
            this.eventHandler = eventHandler;
        }

        @Override
        public boolean stores(final EventHandler<? super T> eventProcessor,
                              final boolean isFilter) {
            return !isFilter && (this.eventHandler == eventProcessor);
        }

        @Override
        public void handleBubblingEvent(final T event) {
            eventHandler.handle(event);
        }

        @Override
        public void handleCapturingEvent(final T event) {
        }

        @Override
        public boolean isDisconnected() {
            return false;
        }
    }

    private static final class WeakEventHandlerRecord<T extends Event>
        extends EventProcessorRecord<T> {
        private final WeakEventHandler<? super T> weakEventHandler;

        public WeakEventHandlerRecord(
            final WeakEventHandler<? super T> weakEventHandler) {
            this.weakEventHandler = weakEventHandler;
        }

        @Override
        public boolean stores(final EventHandler<? super T> eventProcessor,
                              final boolean isFilter) {
            return !isFilter && (weakEventHandler == eventProcessor);
        }

        @Override
        public void handleBubblingEvent(final T event) {
            weakEventHandler.handle(event);
        }

        @Override
        public void handleCapturingEvent(final T event) {
        }

        @Override
        public boolean isDisconnected() {
            return weakEventHandler.wasGarbageCollected();
        }
    }

    private static final class NormalEventFilterRecord<T extends Event>
        extends EventProcessorRecord<T> {
        private final EventHandler<? super T> eventFilter;

        public NormalEventFilterRecord(
            final EventHandler<? super T> eventFilter) {
            this.eventFilter = eventFilter;
        }

        @Override
        public boolean stores(final EventHandler<? super T> eventProcessor,
                              final boolean isFilter) {
            return isFilter && (this.eventFilter == eventProcessor);
        }

        @Override
        public void handleBubblingEvent(final T event) {
        }

        @Override
        public void handleCapturingEvent(final T event) {
            eventFilter.handle(event);
        }

        @Override
        public boolean isDisconnected() {
            return false;
        }
    }

    private static final class WeakEventFilterRecord<T extends Event>
        extends EventProcessorRecord<T> {
        private final WeakEventHandler<? super T> weakEventFilter;

        public WeakEventFilterRecord(
            final WeakEventHandler<? super T> weakEventFilter) {
            this.weakEventFilter = weakEventFilter;
        }

        @Override
        public boolean stores(final EventHandler<? super T> eventProcessor,
                              final boolean isFilter) {
            return isFilter && (weakEventFilter == eventProcessor);
        }

        @Override
        public void handleBubblingEvent(final T event) {
        }

        @Override
        public void handleCapturingEvent(final T event) {
            weakEventFilter.handle(event);
        }

        @Override
        public boolean isDisconnected() {
            return weakEventFilter.wasGarbageCollected();
        }
    }
}

