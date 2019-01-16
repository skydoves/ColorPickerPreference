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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/** Developed by skydoves on 2018-02-11. Copyright (c) 2018 skydoves rights reserved. */
public class MainActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setToolbarTitle("ColorPreference Demo");
  }

  public void example0(View view) {
    startActivity(new Intent(this, ColorPickerViewActivity.class));
  }

  public void example1(View view) {
    startActivity(new Intent(this, ColorPickerDialogActivity.class));
  }

  public void example2(View view) {
    startActivity(new Intent(this, PreferenceActivity.class));
  }
}
