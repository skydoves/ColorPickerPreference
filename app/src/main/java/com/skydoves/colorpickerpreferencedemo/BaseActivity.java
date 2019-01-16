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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

/** Developed by skydoves on 2018-02-12. Copyright (c) 2018 skydoves rights reserved. */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

  private SharedPreferences sharedPreferences;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
  }

  public void setToolbarTitle(String title) {
    Toolbar toolbar = findViewById(R.id.toolbar);
    TextView textView = toolbar.findViewById(R.id.toolbar_title);
    textView.setText(title);

    ImageView back = toolbar.findViewById(R.id.toolbar_home);
    back.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            finish();
          }
        });
  }

  @Override
  protected void onResume() {
    super.onResume();
    setToolbarColor();
    setBackgroundColor();
  }

  /** set toolbar color from DefaultSharedPreferences(PreferenceScreen) */
  public void setToolbarColor() {
    Toolbar toolbar = findViewById(R.id.toolbar);
    toolbar.setBackgroundColor(
        sharedPreferences.getInt(
            getString(R.string.ToolbarColorPickerPreference),
            ContextCompat.getColor(getBaseContext(), R.color.colorPrimary)));
  }

  /** set background color from DefaultSharedPreferences(PreferenceScreen) */
  public void setBackgroundColor() {
    View view = findViewById(R.id.layout_background);
    if (view != null) {
      view.setBackgroundColor(
          sharedPreferences.getInt(
              getString(R.string.BackgroundColorPickerPreference),
              ContextCompat.getColor(getBaseContext(), R.color.background)));
    }
  }
}
