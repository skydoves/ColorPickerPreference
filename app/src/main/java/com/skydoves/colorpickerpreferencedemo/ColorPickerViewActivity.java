package com.skydoves.colorpickerpreferencedemo;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerView;

/**
 * Developed by skydoves on 2018-02-11.
 * Copyright (c) 2018 skydoves rights reserved.
 */

public class ColorPickerViewActivity extends BaseActivity {

    private ColorPickerView colorPickerView;

    private boolean FLAG_PALETTE = false;
    private boolean FLAG_SELECTOR = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker_view);
        setToolbarTitle("ColorPickerViewActivity");

        colorPickerView = findViewById(R.id.colorPickerView);
        colorPickerView.setFlagView(new CustomFlag(this, R.layout.layout_flag));
        colorPickerView.setPreferenceName("MyColorPickerView"); // set PreferenceName, and restore
        colorPickerView.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(ColorEnvelope colorEnvelope) {
                setLayoutColor(colorEnvelope.getColor());
            }
        });
    }

    /**
     * set layout color & textView html code
     * @param color
     */
    private void setLayoutColor(int color) {
        TextView textView = findViewById(R.id.textView);
        textView.setText("#" + colorPickerView.getColorHtml());

        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.setBackgroundColor(color);
    }

    /**
     * change palette drawable resource
     * you must initialize at first in xml
     * @param v view
     */
    public void palette(View v) {
        if(FLAG_PALETTE)
            colorPickerView.setPaletteDrawable(ContextCompat.getDrawable(this, R.drawable.palette));
        else
            colorPickerView.setPaletteDrawable(ContextCompat.getDrawable(this, R.drawable.palettebar));
        FLAG_PALETTE = !FLAG_PALETTE;
    }

    /**
     * change selector drawable resource
     * you must initialize at first in xml
     * @param v view
     */
    public void selector(View v) {
        if(FLAG_SELECTOR)
            colorPickerView.setSelectorDrawable(ContextCompat.getDrawable(this, R.drawable.wheel));
        else
            colorPickerView.setSelectorDrawable(ContextCompat.getDrawable(this, R.drawable.wheel_dark));
        FLAG_SELECTOR = !FLAG_SELECTOR;
    }

    /**
     * moving selector's points (x, y)
     * @param v view
     */
    public void points(View v) {
        int x = (int)(Math.random() * 600) + 100;
        int y = (int)(Math.random() * 400) + 150;
        colorPickerView.setSelectorPoint(x, y);
    }

    /**
     * save selector's positions & the last select color
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        colorPickerView.saveData();
    }
}