
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

public class ColorPickerSharedPreferencesManager {

    private SharedPreferences sharedPreferences;

    public static final String COLOR = "_COLOR";
    public static final String POSITION_X = "_POSITION_X";
    public static final String POSITION_Y = "_POSITION_Y";

    public ColorPickerSharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences("com.skydoves.colorpickerpreference", Context.MODE_PRIVATE);
    }

    public void putInteger(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public int getInteger(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }
}
