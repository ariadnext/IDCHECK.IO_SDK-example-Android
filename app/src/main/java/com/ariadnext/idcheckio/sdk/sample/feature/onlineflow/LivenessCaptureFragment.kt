package com.ariadnext.idcheckio.sdk.sample.feature.onlineflow

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ariadnext.idcheckio.sdk.interfaces.ErrorMsg
import com.ariadnext.idcheckio.sdk.interfaces.IdcheckioInteraction
import com.ariadnext.idcheckio.sdk.interfaces.IdcheckioInteractionInterface
import com.ariadnext.idcheckio.sdk.interfaces.result.IdcheckioResult
import com.ariadnext.idcheckio.sdk.sample.R
import com.ariadnext.idcheckio.sdk.sample.databinding.FragmentLivenessCaptureBinding
import com.ariadnext.idcheckio.sdk.sample.feature.common.BaseFragment
import com.ariadnext.idcheckio.sdk.sample.utils.SdkConfig

/** This fragment do an online liveness session. */
class LivenessCaptureFragment : BaseFragment<FragmentLivenessCaptureBinding>(),
    IdcheckioInteractionInterface {

    override val binding by lazy { FragmentLivenessCaptureBinding.inflate(layoutInflater) }

    /** This contains the result of the previous scan. */
    private val args: LivenessCaptureFragmentArgs by navArgs()

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idcheckioView = SdkConfig.setupSdkForLiveness()
            // The listener which will receive the SDK interaction (such as result, error, ...).
            .listener(this)
            .build()

        // We add the fragment to our view using the child fragment manager and we start it.
        childFragmentManager
            .beginTransaction()
            .replace(R.id.child_container, idcheckioView)
            .commit()

        // We provide the OnlineContext from previous capture to send the folderUid to the startOnline function.
        idcheckioView.startOnline(args.result.onlineContext)

    }

    ///////////////////////////////////////////////////////////////////////////
    // IdcheckioInteractionInterface implementation
    ///////////////////////////////////////////////////////////////////////////

    /**
     * You will receive an interaction from the SDK in this method.
     * By default, you will only receive RESULT and ERROR interaction.
     */
    override fun onIdcheckioInteraction(interaction: IdcheckioInteraction, data: Any?) {
        when (interaction) {
            /**
             * The result will always be an [IdcheckioResult] object.
             * It can't be null but it can be empty. In the case of a liveness,
             * only the 3 following field are available : folderUid, taskUid, documentUid
             */
            IdcheckioInteraction.RESULT -> (data as IdcheckioResult).let {
                findNavController().navigate(
                    LivenessCaptureFragmentDirections.actionLivenessCaptureFragmentToResultFragment(
                        args.result
                    )
                )
            }
            /**
             * You will receive the errors in an [ErrorMsg] object
             * It can't be null.
             */
            IdcheckioInteraction.ERROR -> {
                handleErrorMsg(data as ErrorMsg)
                findNavController().popBackStack()
            }
            else -> {
                /* Do nothing */
            }
        }
    }
}