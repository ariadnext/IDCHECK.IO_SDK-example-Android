package com.ariadnext.idcheckio.sdk.sample.feature.common

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.ariadnext.idcheckio.sdk.sample.R

/** Activity. We do nothing in here. */
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}