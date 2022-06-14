package com.ariadnext.idcheckio.sdk.sample.feature.home

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ariadnext.idcheckio.sdk.sample.R
import com.ariadnext.idcheckio.sdk.sample.databinding.ExampleItemBinding
import com.ariadnext.idcheckio.sdk.sample.feature.bean.SimpleConfig

class ExampleItemViewHolder(
    private val binding: ExampleItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var onConfigSelectionChanged: ((config: SimpleConfig) -> Unit)? = null

    fun setOnConfigChangedListener(listener: (config: SimpleConfig) -> Unit) {
        onConfigSelectionChanged = listener
    }

    fun bind(example: Example) = with(binding) {
        binding.title.setText(example.titleRes)
        if (example.descriptionTopRes != null) {
            binding.descriptionTop.setText(example.descriptionTopRes)
        }
        if (example.descriptionBottomRes != null) {
            binding.descriptionBottom.setText(example.descriptionBottomRes)
        }

        binding.documentSelector.isVisible = example.typeSelectorEnabled
        if (example.typeSelectorEnabled) {
            binding.documentSelector.adapter = ArrayAdapter(
                binding.root.context,
                R.layout.item_simple_spinner,
                SimpleConfig.values()
            )
            binding.documentSelector.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    onConfigSelectionChanged?.invoke(SimpleConfig.values()[position])
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
    }
}