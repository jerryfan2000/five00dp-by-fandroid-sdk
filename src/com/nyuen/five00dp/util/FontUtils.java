package com.nyuen.five00dp.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class FontUtils {

    static final String TAG = "FontUtil";

    public static final String FONT_ROBOTO_LIGHT = "fonts/Roboto-Light.ttf";
    
    public static void setTypeface(Typeface typeface, TextView... textViews) {
        for(TextView each: textViews) {
            if (each != null) {
                each.setTypeface(typeface);
            } 
        }
    }
    
    public static void setTypefaceRobotoLight(Context context, TextView... textViews) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), FONT_ROBOTO_LIGHT);
        setTypeface(typeface, textViews);
    }
}
