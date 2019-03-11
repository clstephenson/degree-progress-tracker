package com.clstephenson.mywgutracker.utils;

import com.clstephenson.mywgutracker.MyApplication;

public class DimensionUtils {

    public static int dpToPx(int dp) {
        float density = MyApplication.getContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

}
