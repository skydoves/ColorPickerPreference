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

package com.skydoves.colorpickerpreferencedemo

import android.app.AlertDialog
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.skydoves.colorpickerpreference.ColorPickerPreference
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.flag.FlagMode
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

@Suppress("LocalVariableName")
class PreferenceFragment : PreferenceFragmentCompat() {

  private val customBuilder: ColorPickerDialog.Builder
    get() {
      val builder = ColorPickerDialog.Builder(activity, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
      builder.setTitle("ColorPicker Dialog")
      builder.colorPickerView.flagView = CustomFlag(activity!!.baseContext, R.layout.layout_flag)
      builder.setPositiveButton(
          getString(R.string.confirm),
          ColorEnvelopeListener { _, _ -> })
      builder.setNegativeButton(
          getString(R.string.cancel)
      ) { dialogInterface, _ -> dialogInterface.dismiss() }

      builder.colorPickerView.flagView.flagMode = FlagMode.LAST
      return builder
    }

  override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
    addPreferencesFromResource(R.xml.pref_settings)
    initColorPickerPreference()
  }

  /** customizing ColorPreference's ColorPickerDialog  */
  private fun initColorPickerPreference() {
    val colorPickerPreference_toolbar = findPreference<ColorPickerPreference>(activity!!.getString(R.string.ToolbarColorPickerPreference))!!
    val builder_toolbar = colorPickerPreference_toolbar.colorPickerDialogBuilder
    if (builder_toolbar != null) {
      builder_toolbar.colorPickerView.flagView = CustomFlag(activity!!.baseContext, R.layout.layout_flag)
    }

    val colorPickerPreference_background = findPreference<ColorPickerPreference>(activity!!.getString(R.string.BackgroundColorPickerPreference))
    if (colorPickerPreference_background != null) {
      colorPickerPreference_background.colorPickerDialogBuilder = customBuilder
    }
  }
}
