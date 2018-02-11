package com.skydoves.colorpickerpreferencedemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Developed by skydoves on 2018-02-12.
 * Copyright (c) 2018 skydoves rights reserved.
 */

public class BaseActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void setToolbarTitle(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbar_title);
        textView.setText(title);

        ImageView back = toolbar.findViewById(R.id.toolbar_home);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToolbarColor();
        setBackgroundColor();
    }

    public void setToolbarColor() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(sharedPreferences.getInt(getString(R.string.ToolbarColorPickerPreference), ContextCompat.getColor(getBaseContext(), R.color.colorPrimary)));
    }

    public void setBackgroundColor() {
        View view = findViewById(R.id.layout_background);
        if(view != null) {
            view.setBackgroundColor(sharedPreferences.getInt(getString(R.string.BackgroundColorPickerPreference),  ContextCompat.getColor(getBaseContext(), R.color.background)));
        }
    }
}
