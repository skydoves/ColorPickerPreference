package com.skydoves.colorpickerpreferencedemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerDialog;
import com.skydoves.colorpickerpreference.ColorPickerPreference;
import com.skydoves.colorpickerpreference.FlagMode;

import androidx.annotation.Nullable;

/**
 * Developed by skydoves on 2018-02-11.
 * Copyright (c) 2018 skydoves rights reserved.
 */

public class PreferenceFragment extends android.preference.PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);
        initColorPickerPreference();
    }

    /**
     * customizing ColorPreference's ColorPickerDialog
     */
    private void initColorPickerPreference() {
        ColorPickerPreference colorPickerPreference_toolbar = (ColorPickerPreference) findPreference(getActivity().getString(R.string.ToolbarColorPickerPreference));
        ColorPickerDialog.Builder builder_toolbar = colorPickerPreference_toolbar.getColorPickerDialogBuilder();
        builder_toolbar.setFlagView(new CustomFlag(getActivity(), R.layout.layout_flag));

        ColorPickerPreference colorPickerPreference_background = (ColorPickerPreference) findPreference(getActivity().getString(R.string.BackgroundColorPickerPreference));
        colorPickerPreference_background.setColorPickerDialogBuilder(getCustomBuilder());
    }

    private ColorPickerDialog.Builder getCustomBuilder() {
        ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("ColorPicker Dialog");
        builder.setFlagView(new CustomFlag(getActivity(), R.layout.layout_flag));
        builder.setPositiveButton(getString(R.string.confirm), new ColorListener() {
            @Override
            public void onColorSelected(ColorEnvelope colorEnvelope) {

            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.getColorPickerView().setFlagMode(FlagMode.LAST);
        return builder;
    }
}
