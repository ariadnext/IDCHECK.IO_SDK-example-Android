package com.ariadnext.idcheckio.sdk.sample.feature.result

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ariadnext.idcheckio.sdk.sample.R
import com.ariadnext.idcheckio.sdk.sample.utils.ImageUtils
import kotlinx.android.synthetic.main.results_item.view.*

/** An adapter to show a viewpager with all the images taken by the SDK */
class ResultsAdapter constructor(private val pathList: List<Uri>) :
    RecyclerView.Adapter<ResultsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return pathList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.results_item.addView(
            ImageUtils.getImageViewFromPath(
                holder.itemView.context,
                pathList[position]
            )
        )
    }

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.results_item, parent, false)
    )
}