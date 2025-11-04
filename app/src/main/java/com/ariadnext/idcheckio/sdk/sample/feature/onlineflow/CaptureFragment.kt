package com.ariadnext.idcheckio.sdk.sample.feature.onlineflow

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.ariadnext.idcheckio.external.error.ErrorMsg
import com.ariadnext.idcheckio.external.result.online.IdcheckioInteraction
import com.ariadnext.idcheckio.external.result.online.IdcheckioInteractionInterface
import com.ariadnext.idcheckio.external.result.online.IdcheckioResult
import com.ariadnext.idcheckio.sdk.sample.R
import com.ariadnext.idcheckio.sdk.sample.databinding.FragmentCaptureBinding
import com.ariadnext.idcheckio.sdk.sample.feature.bean.SimpleConfig
import com.ariadnext.idcheckio.sdk.sample.feature.common.BaseFragment
import com.ariadnext.idcheckio.sdk.sample.utils.ImageUtils
import com.ariadnext.idcheckio.sdk.sample.utils.SdkConfig

class CaptureFragment : BaseFragment<FragmentCaptureBinding>(), IdcheckioInteractionInterface {

    override val binding by lazy { FragmentCaptureBinding.inflate(layoutInflater) }

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // You can configure the document you want to capture here, in our case ID.
        val idcheckioView = SdkConfig.setupSdkByConfig(SimpleConfig.ID)
            // The listener which will receive the SDK interaction (such as result, error, ...).
            .listener(this)
            .build()

        // We add the fragment to our view using the child fragment manager and we start it.
        childFragmentManager.beginTransaction().replace(R.id.child_container, idcheckioView).commit()
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
             * It can't be null but it can be empty.
             *
             * Warning : Between two captures, all the images are cleaned inside the internal SDK cache folder. If
             * you want to keep the images to show them later in your application, you need to copy them to another location.
             */
            IdcheckioInteraction.RESULT -> (data as IdcheckioResult).let {
                ImageUtils.moveImages(requireContext(), it)
                findNavController().navigate(CaptureFragmentDirections.actionCaptureFragmentToResultFragment(it))
            }
            /**
             * You will receive the errors in an [ErrorMsg] object
             * It can't be null.
             */
            IdcheckioInteraction.ERROR -> {
                handleErrorMsg(data as ErrorMsg)
            }
            else -> { /* Do nothing */ }
        }
    }
}