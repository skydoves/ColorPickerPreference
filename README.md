# ColorPickerPreference
[![Build Status](https://travis-ci.org/skydoves/ColorPickerPreference.svg?branch=master)](https://travis-ci.org/skydoves/ColorPickerPreference)
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ColorPickerPreference-green.svg?style=flat)](https://android-arsenal.com/details/1/6759)
[![Android Weekly](https://img.shields.io/badge/Android%20Weekly-%23297-orange.svg)](http://androidweekly.net/issues/issue-297)
<br>
A library that let you implement ColorPickerView, ColorPickerDialog, ColorPickerPreference. <br>
Could get HSV color, RGB values, Html color code from your gallery pictures or custom images just by touching.

![screenshot](https://user-images.githubusercontent.com/24237865/36096666-9e9fc7ca-103a-11e8-9a1a-e1d685d000f9.png)
![gif](https://user-images.githubusercontent.com/24237865/36096667-9ec6693e-103a-11e8-8ed8-1d99da83c9ac.gif)

## Including in your project
#### build.gradle
```java
dependencies {
  compile 'com.github.skydoves:colorpickerpreference:1.0.3'
}
```

## Usage
### ColorPickerView
Could be used just like using ImageView and provides colors from any images.

#### Add XML Namespace
First add below XML Namespace inside your XML layout file.

```xml
xmlns:app="http://schemas.android.com/apk/res-auto"
```

#### ColorPickerView in layout
```xml
<com.skydoves.colorpickerpreference.ColorPickerView
        android:id="@+id/colorPickerView"
        android:layout_width="300dp"
        android:layout_height="300dp"
        app:palette="@drawable/palette"
        app:selector="@drawable/wheel" />
```

#### Attribute description
```
app:palette="@drawable/palette" // set palette image
```

```
app:selector="@drawable/wheel" // set selector image. This isn't required always. If you don't need, don't use.
```

#### Color Selected Listener
```java
colorPickerView.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(ColorEnvelope colorEnvelope) {
                LinearLayout linearLayout = findViewById(R.id.linearLayout);
                linearLayout.setBackgroundColor(colorEnvelope.getColor());
            }
        });
```

#### ColorEnvelope
onColorSelected method tosses a ColorEnvelope's instance. <br>
ColorEnvelope provides HSV color, html color code, rgb. <br>
```java
colorEnvelope.getColor() // int
colorEnvelope.getHtmlCode() // String
colorEnvelope.getRgb() // int[3]
```

#### save and restore
If you want to save selector's position and get selected color in the past, you should set ColorPicker's <br>
Preference name using __setPreferenceName__ method.

```java
colorPickerView.setPreferenceName("MyColorPickerView");
```

And you should save data when you want. <br>
Then selector's position will be restored when ColorPickerView is created.
```java
@Override
protected void onDestroy() {
   super.onDestroy();
   colorPickerView.saveData();
 }
```

And you could get colors saved in the last using below methods. <br>
Below methods need default color(if was not saved any colors it will returns default color) as an argument.
```java
int color = colorPickerView.getSavedColor(Color.WHITE);
String htmlColor = colorPickerView.getSavedColorHtml(Color.WHITE);
int[] colorRGB = colorPickerView.getSavedColorRGB(Color.WHITE); 
```

### ColorPickerDialog
Could be used just like using AlertDialog and provides colors from any images. <br>
It extends AlertDialog, so you could customizing themes also.

```java
ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
builder.setTitle("ColorPicker Dialog");
builder.setPreferenceName("MyColorPickerDialog");
builder.setFlagView(new CustomFlag(this, R.layout.layout_flag));
builder.setPositiveButton(getString(R.string.confirm), new ColorListener() {
   @Override
   public void onColorSelected(ColorEnvelope colorEnvelope) {
      TextView textView = findViewById(R.id.textView);
      textView.setText("#" + colorEnvelope.getHtmlCode());

      LinearLayout linearLayout = findViewById(R.id.linearLayout);
      linearLayout.setBackgroundColor(colorEnvelope.getColor());
     }
   });
   builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
      dialogInterface.dismiss();
   }
});
builder.show();
```

#### save and restore
If you want to save selector's position and get selected color in the past, you should set ColorPicker's <br> 
Preference name using __setPreferenceName__ method. <br>
In case of ColorPickerDialog, saveData() method will be invoked automatically when PositiveButton be selected. <br>
Then selector's position will be restored when be created ColorPickerDialog. so just setting setPreferenceName is done.
```java
ColorPickerView colorPickerView = builder.getColorPickerView();
colorPickerView.setPreferenceName("MyColorPickerDialog");
```

#### set saved color manually
You can set saved color manually.
```java
colorPickerView.setSavedColor(YOUR_COLOR);
```

#### clear saved data
You can clear all of ColorPicker's preference data.
```java
colorPickerView.clearSavedData();
```

### ColorPickerPreference
ColorPickerPreference is used in PreferenceScreen and shows ColorPickerDialog if be clicked.
```xml
<com.skydoves.colorpickerpreference.ColorPickerPreference
    android:key="@string/ToolbarColorPickerPreference"
    android:title="Toolbar Color"
    android:summary="changes toolbar color"
    app:preference_dialog_title="Toolbar ColorPickerDialog"
    app:preference_dialog_positive="@string/confirm"
    app:preference_dialog_negative="@string/cancel"
    app:preference_palette="@drawable/palette"
    app:preference_selector="@drawable/wheel"
    app:default_color="@color/colorPrimary"/>
```

#### customizing
If you want to customizing ColorPickerDialog in ColorPickerPreference, you could get ColorPickerDialog.Builder 
using getColorPickerDialogBuilder() method.
```java
ColorPickerPreference colorPickerPreference_toolbar = (ColorPickerPreference) findPreference(getActivity().getString(R.string.ToolbarColorPickerPreference));
ColorPickerDialog.Builder builder_toolbar = colorPickerPreference_toolbar.getColorPickerDialogBuilder();
builder_toolbar.setFlagView(new CustomFlag(getActivity(), R.layout.layout_flag));
```

### FlagView
FlagView lets you could add a flag above a selector. <br>
First, create Flag layout as your taste like below. <br>

```xml
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

Second, create CustomFlagView extending __FlagView__.
```java
public class CustomFlag extends FlagView {

    private TextView textView;
    private View view;

    public CustomFlag(Context context, int layout) {
        super(context, layout);
        textView = findViewById(R.id.flag_color_code);
        view = findViewById(R.id.flag_color_layout);
    }

    @Override
    public void onRefresh(int color) {
        textView.setText("#" + String.format("%06X", (0xFFFFFF & color)));
        view.setBackgroundColor(color);
    }
}
```

And the last set FlagView on ColorPickerView or ColorPickerDialog.Builder.
```java
colorPickerView.setFlagView(new CustomFlag(this, R.layout.layout_flag));
```
```java
ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
builder.setFlagView(new CustomFlag(this, R.layout.layout_flag));
```

#### Mode
You could set FlagView's showing mode.
```java
colorPickerView.setFlagMode(FlagMode.ALWAYS); // showing always flagView
colorPickerView.setFlagMode(FlagMode.LAST); // showing flagView when touch Action_UP
```

### Methods
#### ColorPickerView
Methods | Return | Description
--- | --- | ---
setColorListener(ColorListener colorlistener) | void | sets ColorListener on ColorPickerView
setPaletteDrawable(Drawable drawable) | void | changes palette drawable resource
setSelectorDrawable(Drawable drawable) | void | changes selector drawable resource
setSelectorPoint(int x, int y) | void | selects selector at point(x, y)
selectCenter() | void | selects center of palette image
setACTION\_UP(Boolean) | void | ColorListener be invoked when ACTION\_UP
setFlagView(FlagView flagview) | void | sets FlagView on ColorPickerView
setFlagMode(FlagMode flagMode) | void | sets FlagMode on ColorPickerView
setFlipable(boolean flipable) | void | sets FlagView be flipbed when go out the ColorPickerView
saveData() | void | save data at local preference. <br> In case of ColorPickerDialog, automatically be invoked when positive button be selected
setPreferenceName(String name) | void | set ColorPicker's preference name. It lets could save and restore selector's positions and the last color
setSavedColor(int color) | void | set ColorPicker's saved color manually.
getColor() | int | returns the last selected color
getColorHtml() | String | returns the last selected Html color code
getColorRGB() | int[3] | returns the last selected color's R/G/B int array
getColorEnvelope() | ColorEnvelope | returns ColorEnvelope. It has the last selected Color, Html, RGB values
getSelectedPoint() | Point | returns the last selected point
getSavedColor(int defaultColor) | int | returns the last saved color
getSavedColorHtml(int defaultColor) | String | returns the last saved Html color code
getSavedColorRGB(int defaultColor) | int[3] | returns the last saved color R/G/B int array
clearSavedData() | void | clear all of colorpicker's preference data

#### ColorPickerDialog.Builder
Methods | Return | Description
--- | --- | ---
setPreferenceName(String name) | void | set ColorPicker's preference name. It lets could save and restore selector's positions and the last color
setFlagView(FlagView flagview) | void | sets FlagView on ColorPickerView
setPositiveButton(CharSequence text, ColorListener colorlistener) | void | sets positive button on ColorPickerDialog
getColorPickerView() | ColorPickerView | returns ColorPickerDialog's ColorPickerView. it lets could customizing or settings ColorPickerView

#### ColorPickerPreference
Methods | Return | Description
--- | --- | ---
setColorPickerDialogBuilder(ColorPickerDialog.Builder builder) | void | sets ColorPickerDialog.Builder as your own build
getColorPickerDialogBuilder() | ColorPickerDialog.Builder | returns ColorPickerDialog.Builder

## Posting & Blog
- [Android Weekly #297](http://androidweekly.net/issues/issue-297)
- [25 new Android libraries, projects and tools worthy to check in Spring 2018](https://medium.com/@mmbialas/25-new-android-libraries-projects-and-tools-worthy-to-check-in-spring-2018-68e3c5e93568)
- [Colour Wheel – Part 1](https://blog.stylingandroid.com/colour-wheel-part-1)

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
