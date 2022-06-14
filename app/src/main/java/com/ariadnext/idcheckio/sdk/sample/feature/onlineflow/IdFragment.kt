package com.ariadnext.idcheckio.sdk.sample.feature.onlineflow

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.ariadnext.idcheckio.sdk.sample.databinding.FragmentIdBinding
import com.ariadnext.idcheckio.sdk.sample.feature.common.BaseFragment

/** Simple fragment used to present the id scan */
class IdFragment : BaseFragment<FragmentIdBinding>() {

    override val binding by lazy { FragmentIdBinding.inflate(layoutInflater) }

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnIdCapture.setOnClickListener {
            findNavController().navigate(IdFragmentDirections.actionIdFragmentToIdCaptureFragment())
        }
    }
}