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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import sun.misc.Unsafe;

/**
 * This class is for breaking the module system of Java 9.
 * @author huang
 */
public class ReflectionHelper {

    private static Unsafe unsafe = null;
    private static long objectFieldOffset;

    static {
        try {
            unsafe = AccessController.doPrivileged((PrivilegedExceptionAction<Unsafe>) () -> {
                Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafe.setAccessible(true);
                return (Unsafe) theUnsafe.get(null);
            });
            Field overrideField = AccessibleObject.class.getDeclaredField("override");
            objectFieldOffset = unsafe.objectFieldOffset(overrideField);
        } catch (Throwable ex) {
        }
    }

    private static void setAccessible(AccessibleObject obj) {
        unsafe.putBoolean(obj, objectFieldOffset, true);
    }

    public static <T> T invoke(Object obj, String methodName) {
        try {
            Method method = obj.getClass().getDeclaredMethod(methodName);
            setAccessible(method);
            return (T) method.invoke(obj);
        } catch (Throwable ex) {
            return null;
        }
    }

    public static <T> T getFieldContent(Object obj, String fieldName) {
        try {
            Field method = obj.getClass().getDeclaredField(fieldName);
            setAccessible(method);
            return (T) method.get(obj);
        } catch (Throwable ex) {
            return null;
        }
    }

    public static <T> T getFieldContent(Class cls, Object obj, String fieldName) {
        try {
            Field method = cls.getDeclaredField(fieldName);
            setAccessible(method);
            return (T) method.get(obj);
        } catch (Throwable ex) {
            return null;
        }
    }
}
