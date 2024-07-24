package com.ariadnext.idcheckio.sdk.sample.utils

import android.content.Context
import android.widget.Toast

object ViewUtils {

    /**
     * Displays a toast message.
     *
     * @param context the context to use
     * @param message the message to display
     */
    fun displayMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
