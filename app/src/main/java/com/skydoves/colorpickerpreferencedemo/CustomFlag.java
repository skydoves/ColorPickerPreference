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

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.FlagView;

/** Developed by skydoves on 2018-02-11. Copyright (c) 2018 skydoves rights reserved. */
public class CustomFlag extends FlagView {

  private TextView textView;
  private View view;

  /**
   * onBind Views
   *
   * @param context context
   * @param layout custom flagView's layout
   */
  public CustomFlag(Context context, int layout) {
    super(context, layout);
    textView = findViewById(R.id.flag_color_code);
    view = findViewById(R.id.flag_color_layout);
  }

  /**
   * invoked when selector moved
   *
   * @param colorEnvelope provide color, htmlCode, rgb
   */
  @Override
  public void onRefresh(ColorEnvelope colorEnvelope) {
    textView.setText("#" + colorEnvelope.getColorHtml());
    view.setBackgroundColor(colorEnvelope.getColor());
  }
}
