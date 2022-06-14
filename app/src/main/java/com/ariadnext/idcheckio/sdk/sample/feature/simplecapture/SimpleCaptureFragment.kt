package com.ariadnext.idcheckio.sdk.sample.feature.simplecapture

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ariadnext.idcheckio.sdk.interfaces.ErrorMsg
import com.ariadnext.idcheckio.sdk.interfaces.IdcheckioInteraction
import com.ariadnext.idcheckio.sdk.interfaces.IdcheckioInteractionInterface
import com.ariadnext.idcheckio.sdk.interfaces.result.IdcheckioResult
import com.ariadnext.idcheckio.sdk.sample.R
import com.ariadnext.idcheckio.sdk.sample.databinding.FragmentSimpleCaptureBinding
import com.ariadnext.idcheckio.sdk.sample.feature.common.BaseFragment
import com.ariadnext.idcheckio.sdk.sample.feature.home.HomeFragment
import com.ariadnext.idcheckio.sdk.sample.utils.SdkConfig
import com.google.android.material.snackbar.Snackbar

/**
 * A simple implementation of the IDCheck.io Sdk.
 * Before starting this fragment, you need to take a look at how to activate the SDK in the [HomeFragment]
 */
class SimpleCaptureFragment : BaseFragment<FragmentSimpleCaptureBinding>(),
    IdcheckioInteractionInterface {

    override val binding by lazy { FragmentSimpleCaptureBinding.inflate(layoutInflater) }

    /**
     * It contains the config you have chosen.
     */
    private val args: SimpleCaptureFragmentArgs by navArgs()

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idcheckioView = SdkConfig.setupSdkByConfig(args.config)
            // The listener which will receive the SDK interaction (such as result, error, ...).
            .listener(this)
            .build()

        // We add the fragment to our view using the child fragment manager and we start it.
        childFragmentManager
            .beginTransaction()
            .replace(R.id.child_container, idcheckioView)
            .commit()
        idcheckioView.start()
    }

    ///////////////////////////////////////////////////////////////////////////
    // IdcheckioInteractionInterface implementation
    ///////////////////////////////////////////////////////////////////////////

    /**
     * You will receive interaction from the SDK in this method.
     * By default, you will only receive Result and Error interaction.
     */
    override fun onIdcheckioInteraction(interaction: IdcheckioInteraction, data: Any?) {
        when (interaction) {
            /**
             * The result will always be an [IdcheckioResult] object.
             * It can't be null but it can be empty.
             */
            IdcheckioInteraction.RESULT -> (data as IdcheckioResult).let {
                findNavController().navigate(
                    SimpleCaptureFragmentDirections.actionSimpleCaptureFragmentToResultFragment(it)
                )
            }
            /**
             * You will receive the errors in an [ErrorMsg] object
             * It can't be null.
             */
            IdcheckioInteraction.ERROR -> {
                Log.e("SimpleCaptureFragment", "SDK Error : ${data as ErrorMsg}")
                Snackbar.make(requireView(), R.string.sdk_error, Snackbar.LENGTH_LONG).show()
                findNavController().popBackStack()
            }
            else -> { /* Do nothing */
            }
        }
    }
}