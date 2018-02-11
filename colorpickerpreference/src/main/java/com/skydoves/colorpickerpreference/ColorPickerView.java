
/*
 * Copyright (C) 2018 skydoves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.colorpickerpreference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ColorPickerView extends FrameLayout {

    private int lastSelectedColor;
    private Point selectedPoint;

    private ImageView palette;
    private ImageView selector;
    private FlagView flagView;

    private Drawable paletteDrawable;
    private Drawable selectorDrawable;

    protected ColorListener mColorListener;

    public enum FlagMode {ALWAYS, LAST, NONE}
    private FlagMode flagMode = FlagMode.ALWAYS;

    private boolean flipable = true;
    private boolean SaveMode = true;
    private boolean ACTON_UP = false;

    private String preferenceName;
    private ColorPickerSharedPreferencesManager sharedPreferencesManager;

    public ColorPickerView(Context context) {
        super(context);
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        getAttrs(attrs);
        onCreate();
    }

    public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        getAttrs(attrs);
        onCreate();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        getAttrs(attrs);
        onCreate();
    }

    private void init() {
        sharedPreferencesManager = new ColorPickerSharedPreferencesManager(getContext());
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                onFirstLayout();
            }
        });
    }

    private void onFirstLayout() {
        if(getSaveMode() && getPreferenceName() != null) {
            int saved_x = sharedPreferencesManager.getInteger(getPreferenceName() + ColorPickerSharedPreferencesManager.POSITION_X, getMeasuredWidth()/2);
            int saved_y = sharedPreferencesManager.getInteger(getPreferenceName() + ColorPickerSharedPreferencesManager.POSITION_Y, getMeasuredHeight()/2);
            setSelectorPoint(saved_x, saved_y);
            handleFlagView(new Point(saved_x - getSelectorHalfWidth(), saved_y - getSelectorHalfHeight()));
        } else
            selectCenter();
        fireColorListener();
        loadListeners();
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ColorPickerView);
        try {
            if (a.hasValue(R.styleable.ColorPickerView_palette))
                paletteDrawable = a.getDrawable(R.styleable.ColorPickerView_palette);
            if (a.hasValue(R.styleable.ColorPickerView_selector))
                selectorDrawable = a.getDrawable(R.styleable.ColorPickerView_selector);
        } finally {
            a.recycle();
        }
    }

    private void onCreate() {
        setPadding(0, 0, 0, 0);
        palette = new ImageView(getContext());
        if (paletteDrawable != null)
            palette.setImageDrawable(paletteDrawable);

        LayoutParams wheelParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        wheelParams.gravity = Gravity.CENTER;
        addView(palette, wheelParams);

        selector = new ImageView(getContext());
        if (selectorDrawable != null) {
            selector.setImageDrawable(selectorDrawable);

            LayoutParams thumbParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            thumbParams.gravity = Gravity.CENTER;
            addView(selector, thumbParams);
        }
    }

    private void loadListeners() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(flagView != null && flagMode == FlagMode.LAST) flagView.gone();
                        if(!ACTON_UP) {
                            selector.setPressed(true);
                            return onTouchReceived(event);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(!ACTON_UP) {
                            selector.setPressed(true);
                            return onTouchReceived(event);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if(flagView != null && flagMode == FlagMode.LAST) flagView.visible();
                        if(ACTON_UP) {
                            selector.setPressed(true);
                            return onTouchReceived(event);
                        }
                        break;
                    default:
                        selector.setPressed(false);
                        return false;
                }
                return true;
            }
        });
    }

    private boolean onTouchReceived(MotionEvent event) {
        Point snapPoint = new Point((int)event.getX(), (int)event.getY());
        int selectedColor = getColorFromBitmap(snapPoint.x, snapPoint.y);

        if(selectedColor != Color.TRANSPARENT) {
            Point centerPoint = getCenterPoint(snapPoint.x, snapPoint.y);
            selector.setX(snapPoint.x - (selector.getMeasuredWidth() / 2));
            selector.setY(snapPoint.y - (selector.getMeasuredHeight() / 2));
            selectedPoint = new Point(snapPoint.x, snapPoint.y);
            lastSelectedColor = selectedColor;
            fireColorListener();
            handleFlagView(centerPoint);
            return true;
        } else
            return false;
    }

    private int getColorFromBitmap(float x, float y) {
        if (paletteDrawable == null) return 0;

        Matrix invertMatrix = new Matrix();
        palette.getImageMatrix().invert(invertMatrix);

        float[] mappedPoints = new float[]{x, y};
        invertMatrix.mapPoints(mappedPoints);

        if (palette.getDrawable() != null && palette.getDrawable() instanceof BitmapDrawable &&
                mappedPoints[0] > 0 && mappedPoints[1] > 0 &&
                mappedPoints[0] < palette.getDrawable().getIntrinsicWidth() && mappedPoints[1] < palette.getDrawable().getIntrinsicHeight()) {

            invalidate();
            return ((BitmapDrawable) palette.getDrawable()).getBitmap().getPixel((int) mappedPoints[0], (int) mappedPoints[1]);
        }
        return 0;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    private void fireColorListener() {
        if (mColorListener != null) {
            mColorListener.onColorSelected(getColorEnvelope());
        }
    }

    private void handleFlagView(Point centerPoint) {
        if (flagView != null && (flagMode == FlagMode.ALWAYS || flagMode == FlagMode.LAST)) {
            if(centerPoint.y - flagView.getHeight() > 0) {
                flagView.setRotation(0);
                if (flagView.getVisibility() == View.GONE) flagView.visible();
                flagView.setX(centerPoint.x - flagView.getWidth() / 2 + selector.getWidth() / 2);
                flagView.setY(centerPoint.y - flagView.getHeight());
                flagView.onRefresh(getColor());
            } else if(getFilpable()) {
                flagView.setRotation(180);
                if (flagView.getVisibility() == View.GONE) flagView.visible();
                flagView.setX(centerPoint.x - flagView.getWidth() / 2 + selector.getWidth() / 2);
                flagView.setY(centerPoint.y + flagView.getHeight() - selector.getHeight() / 2);
                flagView.onRefresh(getColor());
            }
        }
    }

    public void setColorListener(ColorListener colorListener) {
        mColorListener = colorListener;
    }

    public void setPaletteDrawable(Drawable drawable) {
        removeView(palette);
        palette = new ImageView(getContext());
        paletteDrawable = drawable;
        palette.setImageDrawable(paletteDrawable);
        addView(palette);

        removeView(selector);
        addView(selector);

        if(flagMode != FlagMode.NONE && flagView != null) {
            removeView(flagView);
            addView(flagView);
        }

        selectCenter();
    }

    public void setFlagView(FlagView flagView) {
        flagView.gone();
        addView(flagView);
        this.flagView = flagView;
    }

    public void setSelectorDrawable(Drawable drawable) {
        selector.setImageDrawable(drawable);
    }

    public void selectCenter() {
        setSelectorPoint(getMeasuredWidth()/2 - getSelectorHalfWidth(), getMeasuredHeight()/2 - getSelectorHalfHeight());
    }

    public void setSelectorPoint(int x, int y) {
        selector.setX(x - getSelectorHalfWidth());
        selector.setY(y - getSelectorHalfHeight());
        selectedPoint = new Point(x, y);
        lastSelectedColor = getColorFromBitmap(x, y);
        fireColorListener();
        handleFlagView(new Point(x - getSelectorHalfWidth(), y - getSelectorHalfHeight()));
    }

    public void setFlipable(boolean flipable) {
        this.flipable = flipable;
    }

    public void setSaveMode(boolean mode) {
        this.SaveMode = mode;
    }

    public void saveData() {
        if(getSaveMode() && getPreferenceName() != null) {
            sharedPreferencesManager.putInteger(getPreferenceName() + ColorPickerSharedPreferencesManager.POSITION_X, selectedPoint.x);
            sharedPreferencesManager.putInteger(getPreferenceName() + ColorPickerSharedPreferencesManager.POSITION_Y, selectedPoint.y);
            sharedPreferencesManager.putInteger(getPreferenceName() + ColorPickerSharedPreferencesManager.COLOR, lastSelectedColor);
        }
    }

    public void setACTON_UP(boolean value) {
        this.ACTON_UP = value;
    }

    public void setPreferenceName(String preferenceName) {
        this.preferenceName = preferenceName;
    }

    public ColorEnvelope getColorEnvelope() {
        return new ColorEnvelope(getColor(), getColorHtml(), getColorRGB());
    }

    public float getSelectorX() {
        return selector.getX() - getSelectorHalfWidth();
    }

    public int getSelectorHalfWidth() {
        return selector.getMeasuredWidth()/2;
    }

    public int getSelectorHalfHeight() {
        return selector.getMeasuredHeight()/2;
    }

    public float getSelectorY() {
        return selector.getY() - getSelectorHalfHeight();
    }

    public Point getSelectorPoint() {
        return new Point((int)getSelectorX(), (int)getSelectorY());
    }

    public int getColor() {
        return lastSelectedColor;
    }

    public String getColorHtml(){
        return String.format("%06X", (0xFFFFFF & lastSelectedColor));
    }

    public int[] getColorRGB() {
        int[] rgb = new int[3];
        int color = (int) Long.parseLong(String.format("%06X", (0xFFFFFF & lastSelectedColor)), 16);
        rgb[0] = (color >> 16) & 0xFF; // hex to int : R
        rgb[1] = (color >> 8) & 0xFF; // hex to int : G
        rgb[2] = (color >> 0) & 0xFF; // hex to int : B
        return rgb;
    }

    public Point getSelectedPoint() {
        return selectedPoint;
    }

    public boolean getFilpable() {
        return this.flipable;
    }

    public boolean getSaveMode() {
        return this.SaveMode;
    }

    public String getPreferenceName() {
        return this.preferenceName;
    }

    public int getSavedColor(int defaultColor) {
        return sharedPreferencesManager.getInteger(getPreferenceName() + ColorPickerSharedPreferencesManager.COLOR, defaultColor);
    }

    public String getSavedHtml(int defaultColor) {
        return String.format("%06X", (0xFFFFFF & getSavedColor(defaultColor)));
    }

    public int[] getSavedRGB(int defaultColor) {
        int[] rgb = new int[3];
        int color = (int) Long.parseLong(String.format("%06X", (0xFFFFFF & getSavedColor(defaultColor))), 16);
        rgb[0] = (color >> 16) & 0xFF; // hex to int : R
        rgb[1] = (color >> 8) & 0xFF; // hex to int : G
        rgb[2] = (color >> 0) & 0xFF; // hex to int : B
        return rgb;
    }

    private Point getCenterPoint(int x, int y) {
        return new Point(x - (selector.getMeasuredWidth() / 2), y - (selector.getMeasuredHeight() / 2));
    }
}
