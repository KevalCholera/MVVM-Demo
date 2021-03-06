package com.finter.india.utils.floating;

import android.content.Context;
import android.os.Build;

final class Util {

    private Util() {
    }

    static int dpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * scale);
    }

    static boolean hasJellyBean() {
        return true;
    }

    static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
