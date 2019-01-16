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

package com.skydoves.colorpickerpreferencedemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerDialog;
import com.skydoves.colorpickerpreference.ColorPickerPreference;
import com.skydoves.colorpickerpreference.FlagMode;

/** Developed by skydoves on 2018-02-11. Copyright (c) 2018 skydoves rights reserved. */
public class PreferenceFragment extends android.preference.PreferenceFragment {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.pref_settings);
    initColorPickerPreference();
  }

  /** customizing ColorPreference's ColorPickerDialog */
  private void initColorPickerPreference() {
    ColorPickerPreference colorPickerPreference_toolbar =
        (ColorPickerPreference)
            findPreference(getActivity().getString(R.string.ToolbarColorPickerPreference));
    ColorPickerDialog.Builder builder_toolbar =
        colorPickerPreference_toolbar.getColorPickerDialogBuilder();
    builder_toolbar.setFlagView(new CustomFlag(getActivity(), R.layout.layout_flag));

    ColorPickerPreference colorPickerPreference_background =
        (ColorPickerPreference)
            findPreference(getActivity().getString(R.string.BackgroundColorPickerPreference));
    colorPickerPreference_background.setColorPickerDialogBuilder(getCustomBuilder());
  }

  private ColorPickerDialog.Builder getCustomBuilder() {
    ColorPickerDialog.Builder builder =
        new ColorPickerDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
    builder.setTitle("ColorPicker Dialog");
    builder.setFlagView(new CustomFlag(getActivity(), R.layout.layout_flag));
    builder.setPositiveButton(
        getString(R.string.confirm),
        new ColorListener() {
          @Override
          public void onColorSelected(ColorEnvelope colorEnvelope) {}
        });
    builder.setNegativeButton(
        getString(R.string.cancel),
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
          }
        });

    builder.getColorPickerView().setFlagMode(FlagMode.LAST);
    return builder;
  }
}
