package com.ariadnext.idcheckio.sdk.sample.feature.onboarding

import android.os.Bundle
import android.view.View
import com.ariadnext.idcheckio.external.Idcheckio
import com.ariadnext.idcheckio.external.error.ErrorMsg
import com.ariadnext.idcheckio.external.result.activation.InitStatus
import com.ariadnext.idcheckio.external.result.onboarding.OnboardingResultCallback
import com.ariadnext.idcheckio.sdk.sample.databinding.FragmentOnboardingBinding
import com.ariadnext.idcheckio.sdk.sample.feature.common.BaseFragment
import com.ariadnext.idcheckio.sdk.sample.feature.home.HomeFragment
import com.ariadnext.idcheckio.sdk.sample.utils.ViewUtils

/**
 * This example shows how to use the IdCheck.io SDK to start an Onboarding session.
 * You usually doesn't need to create a separate fragment for it.
 * We dit it just so you can enter a folder UID manually.
 * Before starting this fragment, you need to take a look at how to activate the SDK in the [HomeFragment]
 */
class OnboardingFragment : BaseFragment<FragmentOnboardingBinding>(), OnboardingResultCallback {

    override val binding by lazy { FragmentOnboardingBinding.inflate(layoutInflater) }

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startIpsSessionButton.setOnClickListener {
            val folderUid = binding.folderUidField.text.toString()
            // The folder uid is mandatory to start an Onboarding session.
            if (folderUid.isNotEmpty()) {
                // Check if the IdCheck.io SDK is correctly initialize before starting an Onboarding session.
                when (val initStatus = Idcheckio.initStatus) {
                    InitStatus.SUCCESS,
                    InitStatus.WAITING_FOR_RESTART -> startOnboardingSession(folderUid)
                    else -> ViewUtils.displayMessage(requireContext(),"IdCheck.io SDK is not ready. (status=${initStatus.name})")
                }
            } else {
                ViewUtils.displayMessage(requireContext(),"Folder UID is mandatory.")
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // OnboardingResultCallback implementation
    ///////////////////////////////////////////////////////////////////////////

    override fun onSessionSuccess() {
        ViewUtils.displayMessage(requireContext(),"Onboarding session success !")
    }

    override fun onSessionFailure(errorMsg: ErrorMsg) {
        handleErrorMsg(errorMsg)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Starts an Onboarding Session for the given folder UID.
     *
     * @param folderUid the folder uid to send
     */
    private fun startOnboardingSession(folderUid: String) {
        Idcheckio.startOnboarding(
            context = requireContext(),
            folderUid = folderUid,
            resultCallback = this,
        )
    }
}