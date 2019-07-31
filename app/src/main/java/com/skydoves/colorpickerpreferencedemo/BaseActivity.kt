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
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

  private var sharedPreferences: SharedPreferences? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
  }

  fun setToolbarTitle(title: String) {
    val toolbar = findViewById<Toolbar>(R.id.toolbar)
    val textView = toolbar.findViewById<TextView>(R.id.toolbar_title)
    textView.text = title

    val back = toolbar.findViewById<ImageView>(R.id.toolbar_home)
    back.setOnClickListener { finish() }
  }

  override fun onResume() {
    super.onResume()
    setToolbarColor()
    setBackgroundColor()
  }

  /** set toolbar color from DefaultSharedPreferences(PreferenceScreen)  */
  private fun setToolbarColor() {
    val toolbar = findViewById<Toolbar>(R.id.toolbar)
    toolbar.setBackgroundColor(
      sharedPreferences!!.getInt(
        getString(R.string.ToolbarColorPickerPreference),
        ContextCompat.getColor(baseContext, R.color.colorPrimary)))
  }

  /** set background color from DefaultSharedPreferences(PreferenceScreen)  */
  private fun setBackgroundColor() {
    val view = findViewById<View>(R.id.layout_background)
    view?.setBackgroundColor(
      sharedPreferences!!.getInt(
        getString(R.string.BackgroundColorPickerPreference),
        ContextCompat.getColor(baseContext, R.color.background)))
  }
}
