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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
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

@SuppressWarnings({
  "WeakerAccess",
  "unchecked",
  "unused",
  "IntegerDivisionInFloatingPointContext",
  "PointlessBitwiseExpression"
})
public class ColorPickerView extends FrameLayout {

  protected ColorListener mColorListener;
  private int lastSelectedColor;
  private Point selectedPoint;
  private ImageView palette;
  private ImageView selector;
  private FlagView flagView;
  private Drawable paletteDrawable;
  private Drawable selectorDrawable;
  private FlagMode flagMode = FlagMode.ALWAYS;

  private boolean flipable = true;
  private boolean ACTON_UP = false;

  private boolean flagSetPalette = false;
  private float alpha_selector = 1.0f;
  private float alpha_flag = 1.0f;

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
    getViewTreeObserver()
        .addOnGlobalLayoutListener(
            new ViewTreeObserver.OnGlobalLayoutListener() {
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
    if (getPreferenceName() != null) {
      int saved_x =
          sharedPreferencesManager.getInteger(
              getPreferenceName() + ColorPickerSharedPreferencesManager.POSITION_X,
              getMeasuredWidth() / 2);
      int saved_y =
          sharedPreferencesManager.getInteger(
              getPreferenceName() + ColorPickerSharedPreferencesManager.POSITION_Y,
              getMeasuredHeight() / 2);
      setSelectorPoint(saved_x, saved_y);
      handleFlagView(
          new Point(saved_x - getSelectorHalfWidth(), saved_y - getSelectorHalfHeight()));
    } else selectCenter();
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
    if (paletteDrawable != null) palette.setImageDrawable(paletteDrawable);

    LayoutParams wheelParams =
        new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    wheelParams.gravity = Gravity.CENTER;
    addView(palette, wheelParams);

    selector = new ImageView(getContext());
    if (selectorDrawable != null) {
      selector.setImageDrawable(selectorDrawable);

      LayoutParams thumbParams =
          new LayoutParams(
              ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      thumbParams.gravity = Gravity.CENTER;
      addView(selector, thumbParams);
    }
  }

  private void loadListeners() {
    setOnTouchListener(
        new OnTouchListener() {
          @SuppressLint("ClickableViewAccessibility")
          @Override
          public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
              case MotionEvent.ACTION_DOWN:
                if (flagView != null && flagMode == FlagMode.LAST) flagView.gone();
                selector.setPressed(true);
                return onTouchReceived(event);
              case MotionEvent.ACTION_MOVE:
                if (flagView != null && flagMode == FlagMode.LAST) flagView.gone();
                selector.setPressed(true);
                return onTouchReceived(event);
              case MotionEvent.ACTION_UP:
                if (flagView != null && flagMode == FlagMode.LAST) flagView.visible();
                selector.setPressed(true);
                return onTouchReceived(event);
              default:
                selector.setPressed(false);
                return false;
            }
          }
        });
  }

  private boolean onTouchReceived(MotionEvent event) {
    Point snapPoint = new Point((int) event.getX(), (int) event.getY());
    int selectedColor = getColorFromBitmap(snapPoint.x, snapPoint.y);

    if (selectedColor != Color.TRANSPARENT) {
      Point centerPoint = getCenterPoint(snapPoint.x, snapPoint.y);
      selector.setX(snapPoint.x - (selector.getMeasuredWidth() / 2));
      selector.setY(snapPoint.y - (selector.getMeasuredHeight() / 2));
      selectedPoint = new Point(snapPoint.x, snapPoint.y);
      lastSelectedColor = selectedColor;
      handleFlagView(centerPoint);

      if (ACTON_UP) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
          fireColorListener();
        }
      } else {
        fireColorListener();
      }

      return true;
    } else return false;
  }

  private int getColorFromBitmap(float x, float y) {
    if (paletteDrawable == null) return 0;

    Matrix invertMatrix = new Matrix();
    palette.getImageMatrix().invert(invertMatrix);

    float[] mappedPoints = new float[] {x, y};
    invertMatrix.mapPoints(mappedPoints);

    if (palette.getDrawable() != null
        && palette.getDrawable() instanceof BitmapDrawable
        && mappedPoints[0] > 0
        && mappedPoints[1] > 0
        && mappedPoints[0] < palette.getDrawable().getIntrinsicWidth()
        && mappedPoints[1] < palette.getDrawable().getIntrinsicHeight()) {

      invalidate();
      Rect rect = palette.getDrawable().getBounds();
      float scaleX = mappedPoints[0] / rect.height();
      int x1 = (int) (scaleX * ((BitmapDrawable) palette.getDrawable()).getBitmap().getHeight());
      float scaleY = mappedPoints[1] / rect.width();
      int y1 = (int) (scaleY * ((BitmapDrawable) palette.getDrawable()).getBitmap().getWidth());

      return ((BitmapDrawable) palette.getDrawable()).getBitmap().getPixel(x1, y1);
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

      if (flagSetPalette) {
        flagSetPalette = false;
        if (selector != null) {
          selector.setAlpha(alpha_selector);
        }
        if (flagView != null) {
          flagView.setAlpha(alpha_flag);
        }
      }
    }
  }

  private void handleFlagView(Point centerPoint) {
    if (flagView != null) {
      if (flagMode == FlagMode.ALWAYS) flagView.visible();
      if (centerPoint.y - flagView.getHeight() > 0) {
        flagView.setRotation(0);
        flagView.setX(centerPoint.x - flagView.getWidth() / 2 + selector.getWidth() / 2);
        flagView.setY(centerPoint.y - flagView.getHeight());
        flagView.onRefresh(getColorEnvelope());
      } else if (getFilpable()) {
        flagView.setRotation(180);
        flagView.setX(centerPoint.x - flagView.getWidth() / 2 + selector.getWidth() / 2);
        flagView.setY(centerPoint.y + flagView.getHeight() - selector.getHeight() / 2);
        flagView.onRefresh(getColorEnvelope());
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

    if (flagView != null) {
      removeView(flagView);
      addView(flagView);
    }

    // hide selector & flagView
    if (!flagSetPalette) {
      flagSetPalette = true;
      if (selector != null) {
        alpha_selector = selector.getAlpha();
        selector.setAlpha(0.0f);
      }
      if (flagView != null) {
        alpha_flag = flagView.getAlpha();
        flagView.setAlpha(0.0f);
      }
    }
  }

  public void setSelectorDrawable(Drawable drawable) {
    selector.setImageDrawable(drawable);
  }

  public void selectCenter() {
    setSelectorPoint(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
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

  public void setACTON_UP(boolean value) {
    this.ACTON_UP = value;
  }

  public void saveData() {
    if (getPreferenceName() != null) {
      sharedPreferencesManager.putInteger(
          getPreferenceName() + ColorPickerSharedPreferencesManager.POSITION_X, selectedPoint.x);
      sharedPreferencesManager.putInteger(
          getPreferenceName() + ColorPickerSharedPreferencesManager.POSITION_Y, selectedPoint.y);
      sharedPreferencesManager.putInteger(
          getPreferenceName() + ColorPickerSharedPreferencesManager.COLOR, lastSelectedColor);
    }
  }

  public void setSavedColor(int color) {
    sharedPreferencesManager.clearSavedPositions(
        getPreferenceName() + ColorPickerSharedPreferencesManager.POSITION_X,
        getPreferenceName() + ColorPickerSharedPreferencesManager.POSITION_Y);
    sharedPreferencesManager.putInteger(
        getPreferenceName() + ColorPickerSharedPreferencesManager.COLOR, color);
  }

  public Point getSelectedPoint() {
    return selectedPoint;
  }

  private Point getCenterPoint(int x, int y) {
    return new Point(x - (selector.getMeasuredWidth() / 2), y - (selector.getMeasuredHeight() / 2));
  }

  public float getSelectorX() {
    return selector.getX() - getSelectorHalfWidth();
  }

  public float getSelectorY() {
    return selector.getY() - getSelectorHalfHeight();
  }

  public int getSelectorHalfWidth() {
    return selector.getMeasuredWidth() / 2;
  }

  public int getSelectorHalfHeight() {
    return selector.getMeasuredHeight() / 2;
  }

  public int getColor() {
    return lastSelectedColor;
  }

  public String getColorHtml() {
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

  public ColorEnvelope getColorEnvelope() {
    return new ColorEnvelope(getColor(), getColorHtml(), getColorRGB());
  }

  public FlagView getFlagView() {
    return this.flagView;
  }

  public void setFlagView(FlagView flagView) {
    flagView.gone();
    addView(flagView);
    this.flagView = flagView;
  }

  public FlagMode getFlagMode() {
    return this.flagMode;
  }

  public void setFlagMode(FlagMode flagMode) {
    this.flagMode = flagMode;
  }

  public boolean getFilpable() {
    return this.flipable;
  }

  public String getPreferenceName() {
    return this.preferenceName;
  }

  public void setPreferenceName(String preferenceName) {
    this.preferenceName = preferenceName;
  }

  public int getSavedColor(int defaultColor) {
    return sharedPreferencesManager.getInteger(
        getPreferenceName() + ColorPickerSharedPreferencesManager.COLOR, defaultColor);
  }

  public String getSavedColorHtml(int defaultColor) {
    return String.format("%06X", (0xFFFFFF & getSavedColor(defaultColor)));
  }

  public int[] getSavedColorRGB(int defaultColor) {
    int[] rgb = new int[3];
    int color =
        (int) Long.parseLong(String.format("%06X", (0xFFFFFF & getSavedColor(defaultColor))), 16);
    rgb[0] = (color >> 16) & 0xFF; // hex to int : R
    rgb[1] = (color >> 8) & 0xFF; // hex to int : G
    rgb[2] = (color >> 0) & 0xFF; // hex to int : B
    return rgb;
  }

  public void clearSavedData() {
    sharedPreferencesManager.clearSavedData();
  }
}
