package com.ariadnext.idcheckio.sdk.sample.feature.onlineflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ariadnext.idcheckio.sdk.interfaces.ErrorMsg
import com.ariadnext.idcheckio.sdk.interfaces.IdcheckioInteraction
import com.ariadnext.idcheckio.sdk.interfaces.IdcheckioInteractionInterface
import com.ariadnext.idcheckio.sdk.interfaces.result.IdcheckioResult
import com.ariadnext.idcheckio.sdk.sample.R
import com.ariadnext.idcheckio.sdk.sample.feature.overridecapture.OverrideFragment
import com.ariadnext.idcheckio.sdk.sample.utils.SdkConfig
import com.google.android.material.snackbar.Snackbar

/** This fragment do an online liveness session. */
class LivenessCaptureFragment : Fragment(), IdcheckioInteractionInterface {

    /** This contains the result of the previous scan. */
    private val args: LivenessCaptureFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_liveness_capture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idcheckioView = SdkConfig.setupSdkForLiveness()
                // The listener which will receive the SDK interaction (such as result, error, ...).
                .listener(this)
                .build()

        // We add the fragment to our view using the child fragment manager and we start it.
        idcheckioView.let {
            childFragmentManager.beginTransaction().replace(R.id.child_container, it).commit()
            /** We provide the OnlineContext from previous capture to send the folderUid to the startOnline function. */
            it.startOnline(args.result.onlineContext)
        }
    }

    // ---------------------------------------
    // IdcheckioInteractionInterface interface
    // ---------------------------------------

    /**
     * You will receive an interaction from the SDK in this method.
     * By default, you will only receive RESULT and ERROR interaction.
     * To see advanced interaction integration, please take a look at the [OverrideFragment].
     */
    override fun onIdcheckioInteraction(interaction: IdcheckioInteraction, data: Any?) {
        when (interaction) {
            /**
             * The result will always be an [IdcheckioResult] object.
             * It can't be null but it can be empty. In the case of a liveness,
             * only the 3 following field are available : folderUid, taskUid, documentUid
             */
            IdcheckioInteraction.RESULT -> (data as IdcheckioResult).let {
                findNavController().navigate(LivenessCaptureFragmentDirections.actionLivenessCaptureFragmentToResultFragment(args.result))
            }
            /**
             * You will receive the errors in an [ErrorMsg] object
             * It can't be null.
             */
            IdcheckioInteraction.ERROR -> {
                Snackbar.make(requireView(), R.string.sdk_error, Snackbar.LENGTH_LONG).show()
                findNavController().popBackStack()
            }
            else -> { /* Do nothing */
            }
        }
    }
}