package com.ariadnext.idcheckio.sdk.sample.feature.onlineflow

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ariadnext.idcheckio.sdk.sample.databinding.FragmentLivenessBinding
import com.ariadnext.idcheckio.sdk.sample.feature.common.BaseFragment

/** Simple fragment used to present the liveness process. */
class LivenessFragment : BaseFragment<FragmentLivenessBinding>() {

    override val binding by lazy { FragmentLivenessBinding.inflate(layoutInflater) }

    private val args: LivenessFragmentArgs by navArgs()

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLivenessCapture.setOnClickListener {
            findNavController().navigate(
                LivenessFragmentDirections.actionLivenessFragmentToLivenessCaptureFragment(
                    args.result
                )
            )
        }
    }
}