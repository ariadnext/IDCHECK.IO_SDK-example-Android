package com.ariadnext.idcheckio.sdk.sample.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.ariadnext.idcheckio.sdk.sample.R
import com.ariadnext.idcheckio.sdk.sample.feature.bean.SimpleConfig

/**
 * This class is used to build our viewpager with each available example
 */
class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return PAGE_COUNT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val inflater = LayoutInflater.from(holder.itemView.context)
        (holder.itemView as ConstraintLayout).addView(
            inflater.inflate(
                getViewIdFromPosition(position),
                holder.itemView as ConstraintLayout,
                false
            )
        )
        holder.itemView.findViewById<Spinner>(R.id.spinner)?.adapter =
            ArrayAdapter(
                holder.itemView.context,
                R.layout.item_simple_spinner,
                SimpleConfig.values()
            )
    }

    class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.constraint, parent, false)
    )

    private fun getViewIdFromPosition(position: Int): Int {
        return when (position) {
            ONLINE_FLOW -> R.layout.pager_online_flow
            SIMPLE_CAPTURE -> R.layout.pager_simple_capture
            ADVANCED_CAPTURE -> R.layout.pager_advanced_capture
            ANALYZE -> R.layout.pager_analyze
            else -> R.layout.pager_simple_capture
        }
    }

    companion object {
        const val ONLINE_FLOW = 0
        const val SIMPLE_CAPTURE = ONLINE_FLOW + 1
        const val ADVANCED_CAPTURE = SIMPLE_CAPTURE + 1
        const val ANALYZE = ADVANCED_CAPTURE + 1

        private const val PAGE_COUNT = ANALYZE + 1
    }
}