package com.skydoves.colorpickerpreferencedemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Developed by skydoves on 2018-02-11.
 * Copyright (c) 2018 skydoves rights reserved.
 */

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
