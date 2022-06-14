package com.ariadnext.idcheckio.sdk.sample.feature.result

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ariadnext.idcheckio.sdk.sample.databinding.ResultsItemBinding

/** An adapter to show a viewpager with all the images taken by the SDK */
class ResultsAdapter constructor(private val pathList: List<Uri>) :
    RecyclerView.Adapter<ResultsItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsItemViewHolder {
        val binding = ResultsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultsItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return pathList.size
    }

    override fun onBindViewHolder(holder: ResultsItemViewHolder, position: Int) {
        holder.bind(pathList[position])
    }
}