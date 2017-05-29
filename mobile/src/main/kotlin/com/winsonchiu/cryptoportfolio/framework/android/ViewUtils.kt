package com.winsonchiu.cryptoportfolio.framework.android

import android.content.Context
import android.util.TypedValue

/**
 * Created by TheKeeperOfPie on 5/29/2017.
 */

class ViewUtils {
    companion object {
        fun dpToPixels(context: Context, dp: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }
}