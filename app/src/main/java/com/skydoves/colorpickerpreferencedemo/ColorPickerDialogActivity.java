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

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

public class ColorPickerDialogActivity extends BaseActivity {

  @SuppressLint("SetTextI18n")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_color_picker_dialog);
    setToolbarTitle("ColorPickerDialogActivity");
  }

  public void showDialog(View view) {
    ColorPickerDialog.Builder builder =
        new ColorPickerDialog.Builder(this)
            .setTitle("ColorPicker Dialog")
            .setPreferenceName("Test")
            .setPositiveButton(
                getString(R.string.confirm),
                new ColorEnvelopeListener() {
                  @Override
                  public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                    setLayoutColor(envelope);
                  }
                })
            .setNegativeButton(
                getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                  }
                });
    builder.show();
  }

  private void setLayoutColor(ColorEnvelope envelope) {
    TextView textView = findViewById(R.id.textView);
    textView.setText("#" + envelope.getHexCode());

    LinearLayout linearLayout = findViewById(R.id.linearLayout);
    linearLayout.setBackgroundColor(envelope.getColor());
  }
}
