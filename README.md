# ColorPickerPreference
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) <br>
A library that let you implement ColorPicker, ColorPickerDialog, ColorPickerPreference. <br>
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
Could be used just like using ImageView and you can get color from any images.

#### Add XML Namespace
First add below XML Namespace inside your XML layout file.

```xml
xmlns:app="http://schemas.android.com/apk/res-auto"
```

#### ColorPickerView in layout
```xml
<com.skydoves.colorpickerview.ColorPickerView
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
onColorSelected method tosses a ColorEnvelope instance. <br>
You could get HSV color, html color code, rgb from ColorEnvelope. <br>
```java
colorEnvelope.getColor() // int
colorEnvelope.getHtmlCode() // String
colorEnvelope.getRgb() // int[3]
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
