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

package com.jfoenix.controls;

import com.jfoenix.skins.JFXTextFieldSkin;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * JFXTextField is the material design implementation of a text Field.
 *
 * @author Shadi Shaheen
 * @version 1.0
 * @since 2016-03-09
 */
public class JFXTextField extends TextField {
    /**
     * Initialize the style class to 'jfx-text-field'.
     * <p>
     * This is the selector class from which CSS can be used to style
     * this control.
     */
    private static final String DEFAULT_STYLE_CLASS = "jfx-text-field";

    /**
     * {@inheritDoc}
     */
    public JFXTextField() {
        initialize();
    }

    /**
     * {@inheritDoc}
     */
    public JFXTextField(String text) {
        super(text);
        initialize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Skin<?> createDefaultSkin() {
        return new JFXTextFieldSkin(this);
    }

    private void initialize() {
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        if ("dalvik".equals(System.getProperty("java.vm.name").toLowerCase())) {
            this.setStyle("-fx-skin: \"com.jfoenix.android.skins.JFXTextFieldSkinAndroid\";");
        }
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * holds the current active validator on the text field in case of validation error
     */
    private ReadOnlyObjectWrapper<ValidatorBase> activeValidator = new ReadOnlyObjectWrapper<>();

    public ValidatorBase getActiveValidator() {
        return activeValidator == null ? null : activeValidator.get();
    }

    public ReadOnlyObjectProperty<ValidatorBase> activeValidatorProperty() {
        return this.activeValidator.getReadOnlyProperty();
    }

    /**
     * list of validators that will validate the text value upon calling
     * {{@link #validate()}
     */
    private ObservableList<ValidatorBase> validators = FXCollections.observableArrayList();

    public ObservableList<ValidatorBase> getValidators() {
        return validators;
    }

    public void setValidators(ValidatorBase... validators) {
        this.validators.addAll(validators);
    }

    /**
     * validates the text value using the list of validators provided by the user
     * {{@link #setValidators(ValidatorBase...)}
     *
     * @return true if the value is valid else false
     */
    public boolean validate() {
        for (ValidatorBase validator : validators) {
            if (validator.getSrcControl() == null) {
                validator.setSrcControl(this);
            }
            validator.validate();
            if (validator.getHasErrors()) {
                activeValidator.set(validator);
                return false;
            }
        }
        activeValidator.set(null);
        return true;
    }

    public void resetValidation() {
        pseudoClassStateChanged(ValidatorBase.PSEUDO_CLASS_ERROR, false);
        activeValidator.set(null);
    }

    /***************************************************************************
     *                                                                         *
     * styleable Properties                                                    *
     *                                                                         *
     **************************************************************************/

    /**
     * set true to show a float the prompt text when focusing the field
     */
    private StyleableBooleanProperty labelFloat = new SimpleStyleableBooleanProperty(StyleableProperties.LABEL_FLOAT,
        JFXTextField.this,
        "lableFloat",
        false);

    public final StyleableBooleanProperty labelFloatProperty() {
        return this.labelFloat;
    }

    public final boolean isLabelFloat() {
        return this.labelFloatProperty().get();
    }

    public final void setLabelFloat(final boolean labelFloat) {
        this.labelFloatProperty().set(labelFloat);
    }

    /**
     * default color used when the field is unfocused
     */
    private StyleableObjectProperty<Paint> unFocusColor = new SimpleStyleableObjectProperty<>(StyleableProperties.UNFOCUS_COLOR,
        JFXTextField.this,
        "unFocusColor",
        Color.rgb(77,
            77,
            77));

    public Paint getUnFocusColor() {
        return unFocusColor == null ? Color.rgb(77, 77, 77) : unFocusColor.get();
    }

    public StyleableObjectProperty<Paint> unFocusColorProperty() {
        return this.unFocusColor;
    }

    public void setUnFocusColor(Paint color) {
        this.unFocusColor.set(color);
    }

    /**
     * default color used when the field is focused
     */
    private StyleableObjectProperty<Paint> focusColor = new SimpleStyleableObjectProperty<>(StyleableProperties.FOCUS_COLOR,
        JFXTextField.this,
        "focusColor",
        Color.valueOf("#4059A9"));

    public Paint getFocusColor() {
        return focusColor == null ? Color.valueOf("#4059A9") : focusColor.get();
    }

    public StyleableObjectProperty<Paint> focusColorProperty() {
        return this.focusColor;
    }

    public void setFocusColor(Paint color) {
        this.focusColor.set(color);
    }

    /**
     * disable animation on validation
     */
    private StyleableBooleanProperty disableAnimation = new SimpleStyleableBooleanProperty(StyleableProperties.DISABLE_ANIMATION,
        JFXTextField.this,
        "disableAnimation",
        false);

    public final StyleableBooleanProperty disableAnimationProperty() {
        return this.disableAnimation;
    }

    public final Boolean isDisableAnimation() {
        return disableAnimation != null && this.disableAnimationProperty().get();
    }

    public final void setDisableAnimation(final Boolean disabled) {
        this.disableAnimationProperty().set(disabled);
    }


    private static class StyleableProperties {
        private static final CssMetaData<JFXTextField, Paint> UNFOCUS_COLOR = new CssMetaData<JFXTextField, Paint>(
            "-jfx-unfocus-color",
            StyleConverter.getPaintConverter(),
            Color.valueOf("#A6A6A6")) {
            @Override
            public boolean isSettable(JFXTextField control) {
                return control.unFocusColor == null || !control.unFocusColor.isBound();
            }

            @Override
            public StyleableProperty<Paint> getStyleableProperty(JFXTextField control) {
                return control.unFocusColorProperty();
            }
        };
        private static final CssMetaData<JFXTextField, Paint> FOCUS_COLOR = new CssMetaData<JFXTextField, Paint>(
            "-jfx-focus-color",
            StyleConverter.getPaintConverter(),
            Color.valueOf("#3f51b5")) {
            @Override
            public boolean isSettable(JFXTextField control) {
                return control.focusColor == null || !control.focusColor.isBound();
            }

            @Override
            public StyleableProperty<Paint> getStyleableProperty(JFXTextField control) {
                return control.focusColorProperty();
            }
        };
        private static final CssMetaData<JFXTextField, Boolean> LABEL_FLOAT = new CssMetaData<JFXTextField, Boolean>(
            "-jfx-label-float",
            StyleConverter.getBooleanConverter(),
            false) {
            @Override
            public boolean isSettable(JFXTextField control) {
                return control.labelFloat == null || !control.labelFloat.isBound();
            }

            @Override
            public StyleableBooleanProperty getStyleableProperty(JFXTextField control) {
                return control.labelFloatProperty();
            }
        };

        private static final CssMetaData<JFXTextField, Boolean> DISABLE_ANIMATION =
            new CssMetaData<JFXTextField, Boolean>("-jfx-disable-animation",
                StyleConverter.getBooleanConverter(), false) {
                @Override
                public boolean isSettable(JFXTextField control) {
                    return control.disableAnimation == null || !control.disableAnimation.isBound();
                }

                @Override
                public StyleableBooleanProperty getStyleableProperty(JFXTextField control) {
                    return control.disableAnimationProperty();
                }
            };


        private static final List<CssMetaData<? extends Styleable, ?>> CHILD_STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(
                Control.getClassCssMetaData());
            Collections.addAll(styleables, UNFOCUS_COLOR, FOCUS_COLOR, LABEL_FLOAT, DISABLE_ANIMATION);
            CHILD_STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    // inherit the styleable properties from parent
    private List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        if (STYLEABLES == null) {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(
                Control.getClassCssMetaData());
            styleables.addAll(getClassCssMetaData());
            styleables.addAll(TextField.getClassCssMetaData());
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
        return STYLEABLES;
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.CHILD_STYLEABLES;
    }
}
