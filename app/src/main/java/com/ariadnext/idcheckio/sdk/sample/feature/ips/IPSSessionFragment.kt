package com.ariadnext.idcheckio.sdk.sample.feature.ips

import android.os.Bundle
import android.view.View
import com.ariadnext.idcheckio.sdk.bean.DayNightTheme
import com.ariadnext.idcheckio.sdk.bean.IpsCustomization
import com.ariadnext.idcheckio.sdk.bean.Orientation
import com.ariadnext.idcheckio.sdk.component.Idcheckio
import com.ariadnext.idcheckio.sdk.interfaces.ErrorMsg
import com.ariadnext.idcheckio.sdk.interfaces.InitStatus
import com.ariadnext.idcheckio.sdk.interfaces.result.ips.IpsResultCallback
import com.ariadnext.idcheckio.sdk.sample.R
import com.ariadnext.idcheckio.sdk.sample.databinding.FragmentIpsBinding
import com.ariadnext.idcheckio.sdk.sample.feature.common.BaseFragment
import com.ariadnext.idcheckio.sdk.sample.feature.home.HomeFragment
import com.ariadnext.idcheckio.sdk.sample.utils.ViewUtils.Companion.displayMessage

/**
 * This example shows how to use the IdCheck.io SDK to start an IPS session.
 * Before starting this fragment, you need to take a look at how to activate the SDK in the [HomeFragment]
 */
class IPSSessionFragment : BaseFragment<FragmentIpsBinding>(), IpsResultCallback {

    override val binding by lazy { FragmentIpsBinding.inflate(layoutInflater) }

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startIpsSessionButton.setOnClickListener {
            val folderUid = binding.folderUidField.text.toString()
            // The folder uid is mandatory to start an IPS session.
            if (folderUid.isNotEmpty()) {
                // Check if the IdCheck.io SDK is correctly initialize before starting an IPS session.
                when (val initStatus = Idcheckio.initStatus) {
                    InitStatus.SUCCESS,
                    InitStatus.WAITING_FOR_RESTART -> startIpsSession(folderUid)
                    else -> displayMessage(requireContext(),"IdCheck.io SDK is not ready. (status=${initStatus.name})")
                }
            } else {
                displayMessage(requireContext(),"Folder UID is mandatory.")
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // IpsResultCallback implementation
    ///////////////////////////////////////////////////////////////////////////

    override fun onIpsSessionSuccess() {
        displayMessage(requireContext(),"IPS session success !")
    }

    override fun onIpsSessionFailure(errorMsg: ErrorMsg) {
        handleErrorMsg(errorMsg)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Starts an IPS Session for the given folder UID.
     *
     * @param folderUid the folder uid to send
     */
    private fun startIpsSession(folderUid: String) {
        Idcheckio.startIps(
            context = requireContext(),
            folderUid = folderUid,
            resultCallback = this,
            ipsCustomization = IPS_CUSTOMIZATION
        )
    }

    ///////////////////////////////////////////////////////////////////////////
    // Companion
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        /**
         * The IPS session customization object.
         */
        private val IPS_CUSTOMIZATION = IpsCustomization(
            orientation = Orientation.AUTOMATIC,
            theme = DayNightTheme(
                // Color for the main card.
                R.color.ips_foreground_color,
                // Color for document mask and animation border.
                R.color.ips_border_color,
                // Color for main background.
                R.color.ips_background_color,
                // Accent color.
                R.color.ips_primary_color,
                // Text color for titles.
                R.color.ips_title_color,
                // Text color for other text content.
                R.color.ips_text_color
            )
        )
    }
}