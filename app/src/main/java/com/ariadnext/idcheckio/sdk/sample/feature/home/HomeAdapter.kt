package com.ariadnext.idcheckio.sdk.sample.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ariadnext.idcheckio.sdk.sample.databinding.ExampleItemBinding
import com.ariadnext.idcheckio.sdk.sample.feature.bean.SimpleConfig

/**
 * This class is used to build our viewpager with each available example
 */
class HomeAdapter : RecyclerView.Adapter<ExampleItemViewHolder>() {

    private val exampleConfigs: MutableMap<Example, SimpleConfig?> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleItemViewHolder {
        val binding = ExampleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExampleItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return Example.values().size
    }

    override fun onBindViewHolder(holder: ExampleItemViewHolder, position: Int) {
        holder.bind(getExampleAtPosition(position))
        holder.setOnConfigChangedListener { config: SimpleConfig ->
            exampleConfigs[getExampleAtPosition(
                position
            )] = config
        }
    }

    fun getExampleAtPosition(position: Int): Example {
        return Example.values()[position]
    }

    fun getConfigAtPosition(position: Int): SimpleConfig? {
        return exampleConfigs[getExampleAtPosition(position)]
    }
}