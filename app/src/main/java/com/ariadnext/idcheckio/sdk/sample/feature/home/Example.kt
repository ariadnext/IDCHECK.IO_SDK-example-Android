package com.ariadnext.idcheckio.sdk.sample.feature.home

import androidx.annotation.StringRes
import com.ariadnext.idcheckio.sdk.sample.R

enum class Example(
    @StringRes val titleRes: Int,
    @StringRes val descriptionTopRes: Int?,
    @StringRes val descriptionBottomRes: Int?,
    val typeSelectorEnabled: Boolean
) {
    ONLINE_FLOW(
        R.string.home_online_flow_title,
        R.string.home_online_flow_content,
        null,
        false
    ),
    SIMPLE_CAPTURE(
        R.string.home_simple_capture_title,
        R.string.home_simple_capture_choose_doc_type,
        R.string.home_simple_capture_recommended_settings,
        true
    ),
    IPS_CAPTURE(
        R.string.home_ips_capture_title,
        R.string.home_ips_capture_content,
        null,
        false
    ),
    ANALYSE(
        R.string.home_simple_analyze_title,
        R.string.home_analyze_content,
        null,
        false
    )
}