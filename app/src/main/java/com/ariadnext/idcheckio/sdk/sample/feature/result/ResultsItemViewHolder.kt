package com.ariadnext.idcheckio.sdk.sample.feature.result

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.ariadnext.idcheckio.sdk.sample.databinding.ResultsItemBinding
import com.ariadnext.idcheckio.sdk.sample.utils.ImageUtils

class ResultsItemViewHolder(
    private val binding: ResultsItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(imageUri: Uri) = with(binding) {
        this.resultsItem.addView(
            ImageUtils.getImageViewFromPath(root.context, imageUri)
        )
    }
}