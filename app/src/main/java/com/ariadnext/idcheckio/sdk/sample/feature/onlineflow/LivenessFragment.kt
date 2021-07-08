package com.ariadnext.idcheckio.sdk.sample.feature.onlineflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ariadnext.idcheckio.sdk.sample.R
import kotlinx.android.synthetic.main.fragment_liveness.*

/** Simple fragment used to present the liveness process. */
class LivenessFragment : Fragment() {

    private val args: LivenessFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_liveness, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_liveness_capture.setOnClickListener {
            findNavController().navigate(
                LivenessFragmentDirections.actionLivenessFragmentToLivenessCaptureFragment(
                    args.result
                )
            )
        }
    }
}