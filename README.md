# ColorPickerPreference
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) <br>
A library that let you implement ColorPickerView, ColorPickerDialog, ColorPickerPreference. <br>
Could get HSV color, RGB values, Html color code from your gallery pictures or custom images just by touching.

![gif0](https://user-images.githubusercontent.com/24237865/36075630-fc73fef8-0f94-11e8-80d1-b6f7cd4205d5.gif) 
![gif1](https://user-images.githubusercontent.com/24237865/36075656-5a91178c-0f95-11e8-8d19-01d3fe00849e.gif)

## Including in your project
#### build.gradle
```java
dependencies {
  compile 'com.github.skydoves:colorpickerpreference:1.0.1'
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
If you want to save selector's position or get selected color in the past, you should setPreferenceName to ColorPickerView. <br>
```java
colorPickerView.setPreferenceName("MyColorPickerView");
```

And you should save data when you want. <br>
Then selector's position will be restored when be created ColorPickerView.
```java
@Override
protected void onDestroy() {
   super.onDestroy();
   colorPickerView.saveData();
 }
```

### ColorPickerDialog
Could be used just like using AlertDialog and provides colors from any images. <br>
It extends AlertDialog, so you could customizing themes like below.

```java
ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
builder.setTitle("ColorPicker Dialog");
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
If you want to save selector's position or get selected color past, you should setPreferenceName to ColorPickerView. <br>
In case of ColorPickerDialog, colorpickerView.saveData() will be invoked automatically when PositiveButton be pressed. <br>
Then selector's position will be restored when be created ColorPickerDialog. so just setting setPreferenceName is done.
```java
ColorPickerView colorPickerView = builder.getColorPickerView();
colorPickerView.setPreferenceName("MyColorPickerDialog");
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
FlagView lets you could add Flag over selector. <br>
First, Customizing Flag layout as your taste. <br>
Second, create CustomFlagView extending __FlagView__ like below.

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

And the last set FlagView on ColorPickerView. 
```java
colorPickerView.setFlagView(new CustomFlag(this, R.layout.layout_flag));
```

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
