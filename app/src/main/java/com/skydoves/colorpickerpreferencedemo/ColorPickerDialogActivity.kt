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

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class ColorPickerDialogActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_color_picker_dialog)
    setToolbarTitle("ColorPickerDialogActivity")
  }

  fun showDialog(view: View) {
    val builder = ColorPickerDialog.Builder(this)
      .setTitle("ColorPicker Dialog")
      .setPreferenceName("Test")
      .setPositiveButton(
        getString(R.string.confirm),
        ColorEnvelopeListener { envelope, _ -> setLayoutColor(envelope) })
      .setNegativeButton(
        getString(R.string.cancel)
      ) { dialogInterface, i -> dialogInterface.dismiss() }
    builder.show()
  }

  @SuppressLint("SetTextI18n")
  private fun setLayoutColor(envelope: ColorEnvelope) {
    val textView = findViewById<TextView>(R.id.textView)
    textView.text = "#" + envelope.hexCode

    val linearLayout = findViewById<LinearLayout>(R.id.linearLayout)
    linearLayout.setBackgroundColor(envelope.color)
  }
}
