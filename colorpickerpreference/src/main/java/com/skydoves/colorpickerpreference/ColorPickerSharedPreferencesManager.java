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

import android.content.Context;
import android.content.SharedPreferences;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ColorPickerSharedPreferencesManager {

  protected static final String COLOR = "_COLOR";
  protected static final String POSITION_X = "_POSITION_X";
  protected static final String POSITION_Y = "_POSITION_Y";
  private SharedPreferences sharedPreferences;

  protected ColorPickerSharedPreferencesManager(Context context) {
    sharedPreferences =
        context.getSharedPreferences("com.skydoves.colorpickerpreference", Context.MODE_PRIVATE);
  }

  protected void putInteger(String key, int value) {
    sharedPreferences.edit().putInt(key, value).apply();
  }

  protected int getInteger(String key, int defaultValue) {
    return sharedPreferences.getInt(key, defaultValue);
  }

  protected void clearSavedPositions(String positionX, String positionY) {
    sharedPreferences.edit().remove(positionX).apply();
    sharedPreferences.edit().remove(positionY).apply();
  }

  protected void clearSavedData() {
    sharedPreferences.edit().clear().apply();
  }
}
