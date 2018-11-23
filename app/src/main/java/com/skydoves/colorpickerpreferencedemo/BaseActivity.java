package com.skydoves.colorpickerpreferencedemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

    /**
     * set toolbar color from DefaultSharedPreferences(PreferenceScreen)
     */
    public void setToolbarColor() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(sharedPreferences.getInt(getString(R.string.ToolbarColorPickerPreference), ContextCompat.getColor(getBaseContext(), R.color.colorPrimary)));
    }

    /**
     * set background color from DefaultSharedPreferences(PreferenceScreen)
     */
    public void setBackgroundColor() {
        View view = findViewById(R.id.layout_background);
        if(view != null) {
            view.setBackgroundColor(sharedPreferences.getInt(getString(R.string.BackgroundColorPickerPreference),  ContextCompat.getColor(getBaseContext(), R.color.background)));
        }
    }
}
