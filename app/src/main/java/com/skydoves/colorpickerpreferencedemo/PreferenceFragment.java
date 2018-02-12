package com.skydoves.colorpickerpreferencedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.skydoves.colorpickerpreference.ColorPickerDialog;
import com.skydoves.colorpickerpreference.ColorPickerPreference;

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
        ColorPickerDialog.Builder builder_background = colorPickerPreference_background.getColorPickerDialogBuilder();
        builder_background.setFlagView(new CustomFlag(getActivity(), R.layout.layout_flag));
        builder_background.getColorPickerView().setFlipable(false);
        builder_background.getColorPickerView().setACTON_UP(true);
    }
}
