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
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.skydoves.colorpickerview.AlphaTileView
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import com.skydoves.colorpickerview.sliders.AlphaSlideBar
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import java.io.FileNotFoundException

@Suppress("PrivatePropertyName")
class ColorPickerViewActivity : AppCompatActivity() {

  private var colorPickerView: ColorPickerView? = null

  private var FLAG_PALETTE = false
  private var FLAG_SELECTOR = false

  private var powerMenu: PowerMenu? = null
  private val powerMenuItemClickListener = OnMenuItemClickListener<PowerMenuItem> { position, _ ->
    when (position) {
      1 -> palette()
      2 -> paletteFromGallery()
      3 -> selector()
      4 -> dialog()
    }
    powerMenu!!.dismiss()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_color_picker_view)

    powerMenu = PowerMenuUtils.getPowerMenu(this, this, powerMenuItemClickListener)

    colorPickerView = findViewById(R.id.colorPickerView)
    colorPickerView!!.setColorListener(
        ColorEnvelopeListener { envelope, _ -> setLayoutColor(envelope) })

    // attach alphaSlideBar
    val alphaSlideBar = findViewById<AlphaSlideBar>(R.id.alphaSlideBar)
    colorPickerView!!.attachAlphaSlider(alphaSlideBar)

    // attach brightnessSlideBar
    val brightnessSlideBar = findViewById<BrightnessSlideBar>(R.id.brightnessSlide)
    colorPickerView!!.attachBrightnessSlider(brightnessSlideBar)
    colorPickerView!!.setLifecycleOwner(this)
  }

  /**
   * set layout color & textView html code
   *
   * @param envelope ColorEnvelope by ColorEnvelopeListener
   */
  @SuppressLint("SetTextI18n")
  private fun setLayoutColor(envelope: ColorEnvelope) {
    val textView = findViewById<TextView>(R.id.textView)
    textView.text = "#" + envelope.hexCode

    val alphaTileView = findViewById<AlphaTileView>(R.id.alphaTileView)
    alphaTileView.setPaintColor(envelope.color)
  }

  /** shows the popup menu for changing options..  */
  fun overflowMenu(view: View) {
    powerMenu!!.showAsAnchorLeftTop(view)
  }

  /** changes palette image using drawable resource.  */
  private fun palette() {
    if (FLAG_PALETTE)
      colorPickerView!!.setPaletteDrawable(ContextCompat.getDrawable(this, R.drawable.palette)!!)
    else
      colorPickerView!!.setPaletteDrawable(ContextCompat.getDrawable(this, R.drawable.palettebar)!!)
    FLAG_PALETTE = !FLAG_PALETTE
  }

  /** changes palette image from a gallery image.  */
  private fun paletteFromGallery() {
    val photoPickerIntent = Intent(Intent.ACTION_PICK)
    photoPickerIntent.type = "image/*"
    startActivityForResult(photoPickerIntent, 1000)
  }

  /** changes selector image using drawable resource.  */
  private fun selector() {
    if (FLAG_SELECTOR)
      colorPickerView!!.setSelectorDrawable(ContextCompat.getDrawable(this, R.drawable.wheel)!!)
    else
      colorPickerView!!.setSelectorDrawable(ContextCompat.getDrawable(this, R.drawable.wheel_dark)!!)
    FLAG_SELECTOR = !FLAG_SELECTOR
  }

  /** shows ColorPickerDialog  */
  private fun dialog() {
    val builder = ColorPickerDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
        .setTitle("ColorPicker Dialog")
        .setPreferenceName("Test")
        .setPositiveButton(
            getString(R.string.confirm),
            ColorEnvelopeListener { envelope, _ -> setLayoutColor(envelope) })
        .setNegativeButton(
            getString(R.string.cancel)
        ) { dialogInterface, _ -> dialogInterface.dismiss() }
    val colorPickerView = builder.colorPickerView
    colorPickerView.flagView = CustomFlag(this, R.layout.layout_flag)
    builder.show()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    // user choose a picture from gallery
    if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
      try {
        val imageUri = data!!.data
        val imageStream = contentResolver.openInputStream(imageUri!!)
        val selectedImage = BitmapFactory.decodeStream(imageStream)
        val drawable = BitmapDrawable(resources, selectedImage)
        colorPickerView!!.setPaletteDrawable(drawable)
      } catch (e: FileNotFoundException) {
        e.printStackTrace()
      }
    }
  }

  override fun onBackPressed() {
    if (powerMenu!!.isShowing)
      powerMenu!!.dismiss()
    else
      super.onBackPressed()
  }
}
