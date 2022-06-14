package com.ariadnext.idcheckio.sdk.sample.feature.onlineflow

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.ariadnext.idcheckio.sdk.bean.OnlineConfig
import com.ariadnext.idcheckio.sdk.bean.OnlineContext
import com.ariadnext.idcheckio.sdk.interfaces.ErrorMsg
import com.ariadnext.idcheckio.sdk.interfaces.IdcheckioInteraction
import com.ariadnext.idcheckio.sdk.interfaces.IdcheckioInteractionInterface
import com.ariadnext.idcheckio.sdk.interfaces.result.IdcheckioResult
import com.ariadnext.idcheckio.sdk.sample.R
import com.ariadnext.idcheckio.sdk.sample.databinding.FragmentIdCaptureBinding
import com.ariadnext.idcheckio.sdk.sample.feature.common.BaseFragment
import com.ariadnext.idcheckio.sdk.sample.utils.ImageUtils
import com.ariadnext.idcheckio.sdk.sample.utils.SdkConfig

class IdCaptureFragment : BaseFragment<FragmentIdCaptureBinding>(), IdcheckioInteractionInterface {

    override val binding by lazy { FragmentIdCaptureBinding.inflate(layoutInflater) }

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idcheckioView = SdkConfig.setupSdkForOnlineId()
            // The listener which will receive the SDK interaction (such as result, error, ...).
            .listener(this)
            .onlineConfig(
                OnlineConfig(
                    /** Set this Identity document as the reference document for your future liveness session. */
                    isReferenceDocument = true,
                    /**
                     * If you already have a folderUid and if you want to use it, you can set it in the first document capture
                     * of the SDK. The created document will be added to this folder.
                     * If you didn't set it the SDK will create a folder and return it's identifier at the end of the capture in the [OnlineContext].
                     */
                    folderUid = null,
                )
            )
            .build()

        // We add the fragment to our view using the child fragment manager and we start it.
        idcheckioView.let {
            childFragmentManager.beginTransaction().replace(R.id.child_container, it).commit()
            it.startOnline()
        }
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
                findNavController().navigate(
                    IdCaptureFragmentDirections.actionIdCaptureFragmentToLivenessFragment(it)
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