package com.ariadnext.idcheckio.sdk.sample.feature.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.ariadnext.idcheckio.sdk.component.Idcheckio
import com.ariadnext.idcheckio.sdk.interfaces.ErrorMsg
import com.ariadnext.idcheckio.sdk.interfaces.IdcheckioCallback
import com.ariadnext.idcheckio.sdk.sample.BuildConfig
import com.ariadnext.idcheckio.sdk.sample.R
import com.ariadnext.idcheckio.sdk.sample.databinding.FragmentHomeBinding
import com.ariadnext.idcheckio.sdk.sample.feature.common.BaseFragment
import com.google.android.material.snackbar.Snackbar

/**
 * This fragment handle SDK permissions and activation.
 * It will present you the different examples we offer.
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>(), IdcheckioCallback {

    override val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activate()
        binding.apply {
            btnOnline.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCaptureFragment()) }
            btnOnboarding.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToOnboardingFragment()) }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // IdcheckioCallback implementation
    ///////////////////////////////////////////////////////////////////////////

    /**
     * This is the callback interface implementation. We're notified though this function once SDK activation is done.
     * @param success : a boolean flag to check if the SDK activation was successful or not
     * @param error : an eventual error message if the activation failed (invalid licence, no internet access...)
     */
    override fun onInitEnd(success: Boolean, error: ErrorMsg?) {
        if (!success) {
            Snackbar.make(requireView(), R.string.home_init_failed, Snackbar.LENGTH_INDEFINITE)
                .apply {
                    setAction(R.string.common_retry) {
                        activate()
                        dismiss()
                    }
                }.show()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private functions
    ///////////////////////////////////////////////////////////////////////////

    private fun activate() {
        /**
         * Call this method to activate the SDK.
         * It must be called once before starting any capture session.
         */
        Idcheckio.activate(
            // The ID Token is used to authenticate and activate the SDK.
            idToken = BuildConfig.IDCHECKIO_ID_TOKEN,
            // Activation callback (IdcheckioCallback) to receive the SDK's activation result, in our case, it's our fragment.
            callback = this,
            // The application is needed to initialize the SDK components.
            context = requireContext()
        )
    }
}