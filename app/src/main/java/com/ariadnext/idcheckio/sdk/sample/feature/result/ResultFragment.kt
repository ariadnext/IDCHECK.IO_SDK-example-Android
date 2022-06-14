package com.ariadnext.idcheckio.sdk.sample.feature.result

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.ariadnext.idcheckio.sdk.interfaces.result.ImageResult
import com.ariadnext.idcheckio.sdk.sample.databinding.FragmentResultBinding
import com.ariadnext.idcheckio.sdk.sample.feature.common.BaseFragment
import com.ariadnext.idcheckio.sdk.sample.feature.bean.DataView
import com.ariadnext.idcheckio.sdk.sample.utils.ViewUtils
import com.ariadnext.idcheckio.sdk.sample.utils.exhaustive
import com.google.android.material.tabs.TabLayoutMediator
import java.io.File

/**
 * In this fragment we will show the data extracted locally by the sdk.
 */
class ResultFragment : BaseFragment<FragmentResultBinding>() {

    override val binding by lazy { FragmentResultBinding.inflate(layoutInflater) }

    /** We get our results using navigation args */
    private val args: ResultFragmentArgs by navArgs()

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** Set all the images into a view pager */
        binding.vpResults.adapter = ResultsAdapter(getPathList(args.result.images))
        TabLayoutMediator(binding.tlResults, binding.vpResults) { _, _ -> }.attach()
        /** Parse and show the extracted data */
        showDataView()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private functions
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Show all the data extracted or nothing
     * for the document that have no extracted data
     */
    private fun showDataView() {
        val data = ViewUtils.getDataView(args.result)
        for (details in data.fieldList) {
            /** Show every extracted results */
            when (details.fieldType) {
                DataView.FieldType.LABEL -> ViewUtils.addLabelTextView(
                    requireContext(),
                    binding.llResults,
                    details.value as Int
                )
                DataView.FieldType.VALUE -> ViewUtils.addValueTextView(
                    requireContext(),
                    binding.llResults,
                    details.value.toString()
                )
            }.exhaustive
        }
    }

    /** Retrieve all images taken by the SDK */
    private fun getPathList(images: MutableList<ImageResult>): List<Uri> {
        val uriList = ArrayList<Uri>()
        for (image in images) {
            image.cropped.takeIf { it.isNotEmpty() }?.let { uriList.add(Uri.fromFile(File(it))) }
            image.source.takeIf { it.isNotEmpty() }?.let { uriList.add(Uri.fromFile(File(it))) }
            image.face.takeIf { it.isNotEmpty() }?.let { uriList.add(Uri.fromFile(File(it))) }
        }
        return uriList
    }
}