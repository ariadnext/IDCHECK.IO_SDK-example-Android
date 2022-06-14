package com.ariadnext.idcheckio.sdk.sample.utils

import android.content.res.Resources
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

/**
 * When used with "when", forces the use of all the values without the need to use a return value.
 * Coming from https://phauer.com/2019/sealed-classes-exceptions-kotlin/
 */
val <T> T.exhaustive: T
    get() = this

/**
 * Extension method to get the color Int from a color Res.
 * @param color the Color.
 */
@ColorInt
fun Resources.getColorCompat(@ColorRes color: Int) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        getColor(color, null)
    } else {
        @Suppress("DEPRECATION")
        getColor(color)
    }