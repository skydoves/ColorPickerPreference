# ColorPickerPreference
[![Build Status](https://travis-ci.org/skydoves/ColorPickerPreference.svg?branch=master)](https://travis-ci.org/skydoves/ColorPickerPreference)
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ColorPickerPreference-green.svg?style=flat)](https://android-arsenal.com/details/1/6759)
[![Android Weekly](https://img.shields.io/badge/Android%20Weekly-%23297-orange.svg)](http://androidweekly.net/issues/issue-297)
<br>
A library that lets you implement ColorPickerView, ColorPickerDialog, ColorPickerPreference. <br>
Could get HSV color, RGB values, Html color code from your gallery pictures or custom images just by touching.

![screenshot](https://user-images.githubusercontent.com/24237865/36096666-9e9fc7ca-103a-11e8-9a1a-e1d685d000f9.png)
![gif](https://user-images.githubusercontent.com/24237865/36096667-9ec6693e-103a-11e8-8ed8-1d99da83c9ac.gif)

## Including in your project
[ ![Download](https://api.bintray.com/packages/devmagician/maven/colorpickerpreference/images/download.svg) ](https://bintray.com/devmagician/maven/colorpickerpreference/_latestVersion) [![Jitpack](https://jitpack.io/v/skydoves/ColorPickerPreference.svg)](https://jitpack.io/#skydoves/ColorPickerPreference)

#### build.gradle
```gradle
dependencies {
    implementation "com.github.skydoves:colorpickerpreference:2.0.2"
}
```

## Usage
Add following XML namespace inside your XML layout file.

```gradle
xmlns:app="http://schemas.android.com/apk/res-auto"
```

### ColorPickerView in layout
```gradle
<com.skydoves.colorpickerview.ColorPickerView
     android:id="@+id/colorPickerView"
     android:layout_width="300dp"
     android:layout_height="300dp"
     app:palette="@drawable/palette"
     app:selector="@drawable/wheel" />
```

### Attribute descriptions
```gradle
app:palette="@drawable/palette" // sets palette image.
app:selector="@drawable/wheel" // sets selector image.
app:alpha_selector="0.8" // sets selector's alpha.
app:alpha_flag="0.8" // sets flag's alpha.
app:actionMode="last" // sets action mode "always" or "last".
app:preferenceName="MyColorPicker" // sets preference name.
```

### ColorListener
ColorListener is invoked whenever tapped by a user or selecting position manually.
```java
colorPickerView.setColorListener(new ColorListener() {
    @Override
    public void onColorSelected(int color, boolean fromUser) {
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.setBackgroundColor(color);
    }
});
```

### ColorEnvelope Listener
ColorEnvelopeListener provides hsv color, hex code, argb by `ColorEnvelope`.
```java
colorPickerView.setColorListener(new ColorEnvelopeListener() {
    @Override
    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
        linearLayout.setBackgroundColor(envelope.getColor());
        textView.setText("#" + envelope.getHexCode());
    }
});
```

### ColorEnvelope
ColorEnvelope is a wrapper class of colors for provide various forms of color.
```java
colorEnvelope.getColor() // returns a integer color.
colorEnvelope.getHexCode() // returns a hex code string.
colorEnvelope.getArgb() // returns a argb integer array.
```

### ActionMode
ActionMode controls an action about `ColorListener` invoking.
```java
colorPickerView.setActionMode(ActionMode.LAST); // the listener will be invoked only when finger released.
```

### Create using builder
This is how to create `ColorPickerView`'s instance using `ColorPickerView.Builder` class.
```java
ColorPickerView colorPickerView = new ColorPickerView.Builder(context)
      .setColorListener(colorListener)
      .setPreferenceName("MyColorPicker");
      .setActionMode(ActionMode.LAST)
      .setAlphaSlideBar(alphaSlideBar)
      .setBrightnessSlideBar(brightnessSlideBar)
      .setFlagView(new CustomFlag(context, R.layout.layout_flag))
      .setPaletteDrawable(ContextCompat.getDrawable(context, R.drawable.palette))
      .setSelectorDrawable(ContextCompat.getDrawable(context, R.drawable.selector))
      .build();
```

### Restore and save
This is how to restore the status for `ColorPickerView`.<br>
Using `setPreferenceName()` method restores all of the saved statuses automatically.

```java
colorPickerView.setPreferenceName("MyColorPicker");
```

This is how to save the status for `ColorPickerView`.<br>
Using `setLifecycleOwner()` method saves all of the statuses when the lifecycleOwner's destroy automatically.
```java
colorPickerView.setLifecycleOwner(this); // this means activity or fragment.
```
Or we can save the status manually regardless lifecycle using below method.
```java
ColorPickerPreferenceManager.getInstance(this).saveColorPickerData(colorPickerView);
```

### Manipulate and clear
We can manipulate and clear the saved statuses using `ColorPickerPreferenceManager`.
```java
ColorPickerPreferenceManager manager = ColorPickerPreferenceManager.getInstance(this);
manager.setColor("MyColorPicker", Color.RED); // manipulates the saved color data.
manager.setSelectorPosition("MyColorPicker", new Point(120, 120)); // manipulates the saved selector's position data.
manager.clearSavedAllData(); // clears all of the statuses.
manager.clearSavedColor("MyColorPicker"); // clears only saved color data. 
manager.restoreColorPickerData(colorPickerView); // restores the saved statuses manually.
```

### Pallette from Gallery
Here is how to get bitmap drawable from the desired gallery image and set to the palette.<br><br>
<img src="https://user-images.githubusercontent.com/24237865/52941911-313dc000-33ad-11e9-8264-6d78f4ad613a.jpg" align="left" width="35%">

Firstly, declare below permission on your `AndroidManifest.xml` file.
```gradle
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```
startActivityForResult for getting an image from the Gallery.
```java
Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
photoPickerIntent.setType("image/*");
startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
```
On the onActivityResult, we can get a bitmap drawable from the gallery and set it as the palette.
```java
try {
  final Uri imageUri = data.getData();
  final InputStream imageStream = getContentResolver().openInputStream(imageUri);
  final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
  Drawable drawable = new BitmapDrawable(getResources(), selectedImage);
  colorPickerView.setPaletteDrawable(drawable);
} catch (FileNotFoundException e) {
  e.printStackTrace();
}
```

### ColorPickerPreference
ColorPickerPreference is used in PreferenceScreen and shows ColorPickerDialog if be clicked.
```xml
<com.skydoves.colorpickerpreference.ColorPickerPreference
    android:key="@string/ToolbarColorPickerPreference"
    android:title="Toolbar Color"
    android:summary="changes toolbar color"
    app:preference_attachAlphaSlideBar="false" // attach an alpha slide bar or not.
    app:preference_attachBrightnessSlideBar="true" // attach a brightness slide bar or not.
    app:preference_colorBox_radius="26dp" // radius of the color box. we can make it circular using this.
    app:preference_dialog_negative="@string/cancel" // string for closing the dialog.
    app:preference_dialog_positive="@string/confirm" // string for confirming the dialog.
    app:preference_dialog_title="Toolbar ColorPickerDialog" // title string to the dialog.
    app:preference_palette="@drawable/palettebar" // a palette drawable to the ColorPickerView.
    app:preference_selector="@drawable/wheel" // a selector drawable to the ColorPickerView.
```

#### customizing
If you want to customizing ColorPickerDialog in ColorPickerPreference, you could get ColorPickerDialog.Builder 
using getColorPickerDialogBuilder() method.
```java
ColorPickerPreference colorPickerPreference_toolbar = (ColorPickerPreference) findPreference(getActivity().getString(R.string.ToolbarColorPickerPreference));
ColorPickerDialog.Builder builder_toolbar = colorPickerPreference_toolbar.getColorPickerDialogBuilder();
builder_toolbar.setFlagView(new CustomFlag(getActivity(), R.layout.layout_flag));
```

## AlphaSlideBar(Optional)
AlphaSlideBar changes the transparency of the selected color. <br><br>
<img src="https://user-images.githubusercontent.com/24237865/52943592-d1e1af00-33b0-11e9-9e3c-9a1190ae969e.gif" align="left" width="31%">

`AlphaSlideBar` on xml layout
```gradle
<com.skydoves.colorpickerview.sliders.AlphaSlideBar
   android:id="@+id/alphaSlideBar"
   android:layout_width="match_parent"
   android:layout_height="wrap_content"
   app:selector_AlphaSlideBar="@drawable/wheel" // sets the selector drawable.
   app:borderColor_AlphaSlideBar="@android:color/darker_gray" // sets the border color.
   app:borderSize_AlphaSlideBar="5"/> // sets the border size.
```
`attachAlphaSlider` method connects slider to the `ColorPickerView`.

```java
AlphaSlideBar alphaSlideBar = findViewById(R.id.alphaSlideBar);
colorPickerView.attachAlphaSlider(alphaSlideBar);
```
If you want to implement vertically, use below attributes.
```gradle
android:layout_width="280dp" // width should be set manually
android:layout_height="wrap_content"
android:rotation="90"
```

## BrightnessSlideBar(Optional)
BrightnessSlideBar changes the brightness of the selected color. <br><br>
<img src="https://user-images.githubusercontent.com/24237865/52943593-d1e1af00-33b0-11e9-813a-557760e172ed.gif" align="left" width="31%">

`BrightnessSlideBar` on xml layout
```gradle
<com.skydoves.colorpickerview.sliders.BrightnessSlideBar
   android:id="@+id/brightnessSlide"
   android:layout_width="match_parent"
   android:layout_height="wrap_content"
   app:selector_BrightnessSlider="@drawable/wheel" // sets the selector drawable.
   app:borderColor_BrightnessSlider="@android:color/darker_gray" // sets the border color.
   app:borderSize_BrightnessSlider="5"/> // sets the border size.
```
`attachBrightnessSlider` method connects slider to the `ColorPickerView`.

```java
BrightnessSlideBar brightnessSlideBar = findViewById(R.id.brightnessSlide);
colorPickerView.attachBrightnessSlider(brightnessSlideBar);
```
If you want to implement vertically, use below attributes.
```gradle
android:layout_width="280dp" // width should be set manually
android:layout_height="wrap_content"
android:rotation="90"
```

## ColorPickerDialog
![dialog0](https://user-images.githubusercontent.com/24237865/45362890-0d619b80-b611-11e8-857b-e12f82978b53.jpg) 
![dialog1](https://user-images.githubusercontent.com/24237865/45362892-0d619b80-b611-11e8-9cc5-25518a9d392a.jpg) <br>

ColorPickerDialog can be used just like using AlertDialog and provides colors from any drawables. <br>
ColorPickerDialog extends `AlertDialog`. So we can customize themes also. <br>

```java
new ColorPickerDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
      .setTitle("ColorPicker Dialog")
      .setPreferenceName("MyColorPickerDialog")
      .setPositiveButton(getString(R.string.confirm),
          new ColorEnvelopeListener() {
              @Override
              public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                  setLayoutColor(envelope);
              }
          })
       setNegativeButton(getString(R.string.cancel),
          new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                  dialogInterface.dismiss();
              }
           })
      .attachAlphaSlideBar(true) // default is true. If false, do not show the AlphaSlideBar.
      .attachBrightnessSlideBar(true)  // default is true. If false, do not show the BrightnessSlideBar.
      .show();
```

If you need to customize the `ColorpickerView` on the dialog, you can get the instance of `ColorPickerView` from the `ColorpickerView.Builder`. but it should be done before shows dialog using `show()` method.
```java
ColorPickerView colorPickerView = builder.getColorPickerView();
colorPickerView.setFlagView(new CustomFlag(this, R.layout.layout_flag)); // sets a custom flagView
builder.show(); // shows the dialog
```

## FlagView(Optional)
![flag0](https://user-images.githubusercontent.com/24237865/45364191-75fe4780-b614-11e8-81a5-04690a4392db.jpg) 
![flag1](https://user-images.githubusercontent.com/24237865/45364194-75fe4780-b614-11e8-844c-136d14c91560.jpg) <br>

FlgaView implements showing a flag above a selector. This is optional.<br><br>
First, create a layout for `FlagView` as your taste like below. 
```gradle
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="100dp"
    android:layout_height="40dp"
    android:background="@drawable/flag"
    android:orientation="horizontal">

    <LinearLayout
        android:id="@+id/flag_color_layout"
        android:layout_width="20dp"
        android:layout_height="20dp"
        android:layout_marginTop="6dp"
        android:layout_marginLeft="5dp"
        android:orientation="vertical"/>

    <TextView
        android:id="@+id/flag_color_code"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="6dp"
        android:layout_marginLeft="10dp"
        android:layout_marginRight="5dp"
        android:textSize="14dp"
        android:textColor="@android:color/white"
        android:maxLines="1"
        android:ellipsize="end"
        android:textAppearance="?android:attr/textAppearanceSmall"
        tools:text="#ffffff"/>
</LinearLayout>
```

Second, create a custom class extending `FlagView`. This is an example code.
```java
public class CustomFlag extends FlagView {

    private TextView textView;
    private AlphaTileView alphaTileView;

    public CustomFlag(Context context, int layout) {
        super(context, layout);
        textView = findViewById(R.id.flag_color_code);
        alphaTileView = findViewById(R.id.flag_color_layout);
    }

    @Override
    public void onRefresh(ColorEnvelope colorEnvelope) {
        textView.setText("#" + colorEnvelope.getHexCode());
        alphaTileView.setPaintColor(colorEnvelope.getColor());
    }
}
```

The last, set `FlagView` on the `ColorPickerView` using `setFlagView` method.

```java
colorPickerView.setFlagView(new CustomFlag(this, R.layout.layout_flag));
```

### FlagMode
FlagMode decides the `FlagView`'s visibility action.
```java
colorPickerView.setFlagMode(FlagMode.ALWAYS); // showing always by tapping and dragging.
colorPickerView.setFlagMode(FlagMode.LAST); // showing only when finger released.
```

## AlphaTileView
![alphatileview](https://user-images.githubusercontent.com/24237865/45364416-09377d00-b615-11e8-9707-b83f55053480.jpg) <br>
AlphaTileView visualizes ARGB color on the view. If you want to visualizes ARGB color on the general view, it will not be shown accurately. because it will be mixed with the parent view's background color. so if you want to visualize ARGB color accurately, should use `AlphaTileView`.<br>

```gradle
<com.skydoves.colorpickerview.AlphaTileView
     android:id="@+id/alphaTileView"
     android:layout_width="55dp"
     android:layout_height="55dp"
     app:tileSize="20" // the size of the repeating tile
     app:tileEvenColor="@color/tile_even" // the color of even tiles
     app:tileOddColor="@color/tile_odd"/> // the color of odd tiles
```

### ColorPickerView Methods
Methods | Return | Description
--- | --- | ---
getColor() | int | gets the last selected color.
getColorEnvelope() | ColorEnvelope | gets the `ColorEnvelope` of the last selected color.
setPaletteDrawable(Drawable drawable) | void | changes palette drawable manually.
setSelectorDrawable(Drawable drawable) | void | changes selector drawable manually.
setSelectorPoint(int x, int y) | void | selects the specific coordinate of the palette manually.
selectCenter() | void | selects the center of the palette manually.
setActionMode(ActionMode) | void | sets the color listener's trigger action mode.
setFlagView(FlagView flagview) | void | sets `FlagView` on `ColorPickerView`.
attachAlphaSlider | void | linking an `AlphaSlideBar` on the `ColorPickerView`.
attachBrightnessSlider | void | linking an `BrightnessSlideBar` on the `ColorPickerView`.

#### ColorPickerPreference
Methods | Return | Description
--- | --- | ---
setColorPickerDialogBuilder(ColorPickerDialog.Builder builder) | void | sets ColorPickerDialog.Builder as your own build
getColorPickerDialogBuilder() | ColorPickerDialog.Builder | returns ColorPickerDialog.Builder

## Posting & Blog
- [Android Weekly #297](http://androidweekly.net/issues/issue-297)
- [25 new Android libraries, projects and tools worthy to check in Spring 2018](https://medium.com/@mmbialas/25-new-android-libraries-projects-and-tools-worthy-to-check-in-spring-2018-68e3c5e93568)
- [Colour Wheel – Part 1](https://blog.stylingandroid.com/colour-wheel-part-1)
- [Using ColorPickerPreference library in Android Studio - Youtube](https://www.youtube.com/watch?v=_q7a0EPbGvU)

## Find this library useful? :heart:
Support it by joining [stargazers](https://github.com/skydoves/ColorPickerPreference/stargazers) for this repository. :star:

## Supports :coffee:
If you feel like support me a coffee for my efforts, I would greatly appreciate it. <br><br>
<a href="https://www.buymeacoffee.com/skydoves" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/purple_img.png" alt="Buy Me A Coffee" style="height: auto !important;width: auto !important;" ></a>

# License
```xml
Copyright 2018 skydoves

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
