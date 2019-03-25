/*
 * Copyright (C) 2018 skydoves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.colorpickerpreference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

@SuppressWarnings("unused")
public class ColorPickerPreference extends Preference {

  private View colorBox;
  private ColorPickerDialog.Builder builder;
  private AlertDialog alertDialog;

  private int default_color;
  private Drawable paletteDrawable;
  private Drawable selectorDrawable;
  private String title;
  private String positive;
  private String negative;

  public ColorPickerPreference(Context context) {
    super(context);
  }

  public ColorPickerPreference(Context context, AttributeSet attrs) {
    super(context, attrs);
    getAttrs(attrs);
    onInit();
  }

  public ColorPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    getAttrs(attrs, defStyleAttr);
    onInit();
  }

  private void getAttrs(AttributeSet attrs) {
    TypedArray typedArray =
        getContext().obtainStyledAttributes(attrs, R.styleable.ColorPickerPreference);
    setTypeArray(typedArray);
  }

  private void getAttrs(AttributeSet attrs, int defStyle) {
    TypedArray typedArray =
        getContext().obtainStyledAttributes(attrs, R.styleable.ColorPickerPreference, defStyle, 0);
    setTypeArray(typedArray);
  }

  private void setTypeArray(TypedArray typedArray) {
    default_color =
        typedArray.getColor(R.styleable.ColorPickerPreference_default_color, Color.BLACK);
    paletteDrawable = typedArray.getDrawable(R.styleable.ColorPickerPreference_preference_palette);
    selectorDrawable =
        typedArray.getDrawable(R.styleable.ColorPickerPreference_preference_selector);
    title = typedArray.getString(R.styleable.ColorPickerPreference_preference_dialog_title);
    positive = typedArray.getString(R.styleable.ColorPickerPreference_preference_dialog_positive);
    negative = typedArray.getString(R.styleable.ColorPickerPreference_preference_dialog_negative);
  }

  private void onInit() {
    setWidgetLayoutResource(R.layout.layout_colorpicker_preference);
    setColorPickerDialogBuilder(new ColorPickerDialog.Builder(getContext()));
  }

  @Override
  public void onBindViewHolder(PreferenceViewHolder holder) {
    super.onBindViewHolder(holder);
    colorBox = holder.findViewById(R.id.colorpicker_preference_colorbox);
    colorBox.setBackgroundColor(
        getPreferenceManager().getSharedPreferences().getInt(getKey(), default_color));
  }

  @Override
  protected void onClick() {
    super.onClick();

    if (alertDialog != null) {
      alertDialog.show();
    }
  }

  public ColorPickerDialog.Builder getColorPickerDialogBuilder() {
    return this.builder;
  }

  public void setColorPickerDialogBuilder(ColorPickerDialog.Builder builder) {
    this.builder = builder;
    this.builder.setTitle(title);
    this.builder.setPositiveButton(
        positive,
        new ColorListener() {
          @Override
          public void onColorSelected(ColorEnvelope colorEnvelope) {
            if (colorBox != null) {
              colorBox.setBackgroundColor(colorEnvelope.getColor());
              getPreferenceManager()
                  .getSharedPreferences()
                  .edit()
                  .putInt(getKey(), colorEnvelope.getColor())
                  .apply();
            }
          }
        });
    this.builder.setNegativeButton(
        negative,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
          }
        });

    ColorPickerView colorPickerView = builder.getColorPickerView();
    colorPickerView.setPaletteDrawable(paletteDrawable);
    colorPickerView.setSelectorDrawable(selectorDrawable);
    colorPickerView.setPreferenceName(getKey());
    this.alertDialog = builder.create();
  }
}
