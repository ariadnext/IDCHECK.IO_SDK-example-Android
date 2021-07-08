package com.ariadnext.idcheckio.sdk.sample.feature.onlineflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ariadnext.idcheckio.sdk.sample.R
import kotlinx.android.synthetic.main.fragment_id.*

/** Simple fragment used to present the id scan */
class IdFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_id, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_id_capture.setOnClickListener {
            findNavController().navigate(IdFragmentDirections.actionIdFragmentToIdCaptureFragment())
        }
    }
}