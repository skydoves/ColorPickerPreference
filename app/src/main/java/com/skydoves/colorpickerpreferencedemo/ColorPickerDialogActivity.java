package com.skydoves.colorpickerpreferencedemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerDialog;
import com.skydoves.colorpickerpreference.ColorPickerView;

/**
 * Developed by skydoves on 2018-02-11.
 * Copyright (c) 2018 skydoves rights reserved.
 */

public class ColorPickerDialogActivity extends BaseActivity {

    private AlertDialog alertDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker_dialog);
        setToolbarTitle("ColorPickerDialogActivity");

        ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("ColorPicker Dialog");
        builder.setPreferenceName("MyColorPickerDialog");
        builder.setFlagView(new CustomFlag(this, R.layout.layout_flag));
        builder.setPositiveButton(getString(R.string.confirm), new ColorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onColorSelected(ColorEnvelope colorEnvelope) {
                TextView textView = findViewById(R.id.textView);
                textView.setText("#" + colorEnvelope.getColorHtml());

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

        alertDialog = builder.create();

        /* get ColorPicker from builder, and set views as saved data */
        ColorPickerView colorPickerView = builder.getColorPickerView();

        TextView textView = findViewById(R.id.textView);
        textView.setText("#" + colorPickerView.getSavedColorHtml(R.color.colorAccent));

        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.setBackgroundColor(colorPickerView.getSavedColor(R.color.colorAccent));
    }

    public void showDialog(View view) {
        alertDialog.show();
    }
}
