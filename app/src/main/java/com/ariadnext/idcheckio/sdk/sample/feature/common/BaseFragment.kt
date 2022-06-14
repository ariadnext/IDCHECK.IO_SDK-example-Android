package com.ariadnext.idcheckio.sdk.sample.feature.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ariadnext.idcheckio.sdk.interfaces.ErrorMsg
import com.ariadnext.idcheckio.sdk.interfaces.IdcheckioErrorCause
import com.ariadnext.idcheckio.sdk.interfaces.IdcheckioSubCause
import com.ariadnext.idcheckio.sdk.sample.utils.ViewUtils.Companion.displayMessage

/**
 * Base fragment to be extended by feature fragments.
 *
 * Provides:
 * - easy layout declaration
 */
abstract class BaseFragment<FragmentViewBinding : ViewBinding> : Fragment() {

    abstract val binding: FragmentViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    fun handleErrorMsg(errorMsg: ErrorMsg) {
        Log.e("BaseFragment", "SDK Error : $errorMsg")
        when (errorMsg.subCause) {
            // The user didn't grand camera or audio permission to the app.
            IdcheckioSubCause.MISSING_PERMISSIONS -> displayMessage(requireContext(), "Error: Camera permission need to be granted.")
            // The user cancelled the scanning session.
            IdcheckioSubCause.CANCELLED_BY_USER -> displayMessage(requireContext(), "Error: The session has been canceled.")
            // The document model has been rejected by the customer.
            IdcheckioSubCause.MODEL_REJECTED -> displayMessage(requireContext(), "Error: The document type is not accepted for this session.")
            // The document is expired and the customer has rejected expired document scanning.
            IdcheckioSubCause.ANALYSIS_EXPIRED_DOC -> displayMessage(requireContext(), "Error: The document is expired.")
            // The document is not eligible for IPS session.
            IdcheckioSubCause.PVID_NOT_ELIGIBLE -> displayMessage(
                requireContext(),
                "Error: The document is not supported for PVID processing. Please provide a valid identity document."
            )
            null -> when (errorMsg.cause) {
                // The customer has made a mistake configuring the SDK (missing licence, invalid parameters...).
                IdcheckioErrorCause.CUSTOMER_ERROR -> displayMessage(requireContext(), "Implementation error: ${errorMsg.message}")
                // There seems to be a network error.
                IdcheckioErrorCause.NETWORK_ERROR -> displayMessage(requireContext(), "Network error: ${errorMsg.message}")
                // The user has failed the capture.
                IdcheckioErrorCause.USER_ERROR -> displayMessage(requireContext(), "User error: ${errorMsg.message}")
                // Something happened on our servers.
                IdcheckioErrorCause.INTERNAL_ERROR -> displayMessage(requireContext(), "Internal error: ${errorMsg.message}")
                // Hardware error from the user's device.
                IdcheckioErrorCause.DEVICE_ERROR -> displayMessage(requireContext(), "Device error: ${errorMsg.message}")
                // Invalid document.
                IdcheckioErrorCause.DOCUMENT_ERROR -> displayMessage(requireContext(), "Document error: ${errorMsg.message}")
            }
        }
    }

}