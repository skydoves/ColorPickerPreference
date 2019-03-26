package com.skydoves.colorpickerpreferencedemo;


import android.content.Context;
import android.graphics.Color;
import androidx.lifecycle.LifecycleOwner;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.MenuEffect;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

@SuppressWarnings("WeakerAccess")
public class PowerMenuUtils {
  public static PowerMenu getPowerMenu(
      Context context,
      LifecycleOwner lifecycleOwner,
      OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener) {
    return new PowerMenu.Builder(context)
        .setHeaderView(R.layout.layout_header)
        .addItem(new PowerMenuItem("Palette", false))
        .addItem(new PowerMenuItem("Palette(Gallery)", false))
        .addItem(new PowerMenuItem("Selector", false))
        .addItem(new PowerMenuItem("Dialog", false))
        .setLifecycleOwner(lifecycleOwner)
        .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT)
        .setMenuEffect(MenuEffect.BODY)
        .setMenuRadius(10f)
        .setMenuShadow(10f)
        .setTextColor(context.getResources().getColor(R.color.md_grey_800))
        .setSelectedEffect(false)
        .setShowBackground(false)
        .setMenuColor(Color.WHITE)
        .setOnMenuItemClickListener(onMenuItemClickListener)
        .build();
  }
}
