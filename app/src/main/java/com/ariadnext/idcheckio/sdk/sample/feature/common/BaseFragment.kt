package com.ariadnext.idcheckio.sdk.sample.feature.common

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.ariadnext.idcheckio.sdk.interfaces.ErrorMsg
import com.ariadnext.idcheckio.sdk.interfaces.IdcheckioErrorCause
import com.ariadnext.idcheckio.sdk.interfaces.IdcheckioSubCause
import com.ariadnext.idcheckio.sdk.sample.utils.ViewUtils

/**
 * Base fragment to be extended by feature fragments.
 *
 * Provides:
 * - easy layout declaration
 */
abstract class BaseFragment<FragmentViewBinding : ViewBinding> : Fragment() {

    abstract val binding: FragmentViewBinding

    /**
     * Is the app have been stopped by Android
     * We store the information that the sdk has finished in error to back to the previous page (or error page)
     */
    private var waitingForBack = false
    /**
     * We also store the error message to display it
     */
    private var errorMsg: ErrorMsg? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onResume() {
        super.onResume()
        if(waitingForBack) {
            waitingForBack = false
            errorMsg?.let { doHandleErrorMsg(it) }
        }
    }

    /**
     * Verify that the context still exist and show the error if it exists.
     * Otherwise store the error to show it later.
     * @param errorMsg the error to display
     */
    fun handleErrorMsg(errorMsg: ErrorMsg) {
        // Only use navigation if the view is currently alive
        if(!lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            this.errorMsg = errorMsg
            waitingForBack = true
            return
        }
        doHandleErrorMsg(errorMsg)
    }
    /**
     * Show a given error and back to previous screen
     * @param errorMsg the error to display
     */
    private fun doHandleErrorMsg(errorMsg: ErrorMsg) {
        Log.e("BaseFragment", "SDK Error : $errorMsg")
        when (errorMsg.subCause) {
            // The user didn't grand camera or audio permission to the app.
            IdcheckioSubCause.MISSING_PERMISSIONS -> displayMessageAndBack(requireContext(), "Error: Camera permission need to be granted.")
            // The user cancelled the scanning session.
            IdcheckioSubCause.CANCELLED_BY_USER -> displayMessageAndBack(requireContext(), "Error: The session has been canceled.")
            // The document model has been rejected by the customer.
            IdcheckioSubCause.MODEL_REJECTED -> displayMessageAndBack(requireContext(), "Error: The document type is not accepted for this session.")
            // The document is expired and the customer has rejected expired document scanning.
            IdcheckioSubCause.ANALYSIS_EXPIRED_DOC -> displayMessageAndBack(requireContext(), "Error: The document is expired.")
            // The document is not eligible for IPS session.
            IdcheckioSubCause.PVID_NOT_ELIGIBLE -> displayMessageAndBack(requireContext(), "Error: The document is not supported for PVID processing. Please provide a valid identity document.")
            // We failed to identify the document
            IdcheckioSubCause.UNIDENTIFIED -> displayMessageAndBack(requireContext(), "Error: The document couldn't be identified.")
            // The document does not contain a face and can't be used for face recognition
            IdcheckioSubCause.DOC_NOT_USABLE_FACEREC -> displayMessageAndBack(requireContext(), "Error: The document does not contain a face and can't be used for facial recognition.")
            // The document uploaded is not supported.
            IdcheckioSubCause.UNSUPPORTED_FILE_EXTENSION -> displayMessageAndBack(requireContext(), "Error: The provided file format is not supported.")
            null -> when (errorMsg.cause) {
                // The customer has made a mistake configuring the SDK (missing licence, invalid parameters...).
                IdcheckioErrorCause.CUSTOMER_ERROR -> displayMessageAndBack(requireContext(), "Implementation error: ${errorMsg.message}")
                // There seems to be a network error.
                IdcheckioErrorCause.NETWORK_ERROR -> displayMessageAndBack(requireContext(), "Network error: ${errorMsg.message}")
                // The user has failed the capture.
                IdcheckioErrorCause.USER_ERROR -> displayMessageAndBack(requireContext(), "User error: ${errorMsg.message}")
                // Something happened on our servers.
                IdcheckioErrorCause.INTERNAL_ERROR -> displayMessageAndBack(requireContext(), "Internal error: ${errorMsg.message}")
                // Hardware error from the user's device.
                IdcheckioErrorCause.DEVICE_ERROR -> displayMessageAndBack(requireContext(), "Device error: ${errorMsg.message}")
                // Invalid document.
                IdcheckioErrorCause.DOCUMENT_ERROR -> displayMessageAndBack(requireContext(), "Document error: ${errorMsg.message}")
            }
        }
    }

    /**
     * Displays a toast message and then back to the main page.
     *
     * @param context the context to use
     * @param message the message to display
     */
    private fun displayMessageAndBack(context: Context, message: String) {
        ViewUtils.displayMessage(context, message)
        findNavController().popBackStack()
    }

}