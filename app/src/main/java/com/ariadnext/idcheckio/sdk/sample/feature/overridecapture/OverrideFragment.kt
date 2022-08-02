package com.ariadnext.idcheckio.sdk.sample.feature.overridecapture

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ariadnext.idcheckio.sdk.bean.Orientation
import com.ariadnext.idcheckio.sdk.bean.Quad
import com.ariadnext.idcheckio.sdk.interfaces.*
import com.ariadnext.idcheckio.sdk.interfaces.result.Document
import com.ariadnext.idcheckio.sdk.interfaces.result.IdcheckioResult
import com.ariadnext.idcheckio.sdk.module.ui.command.EnumCommand
import com.ariadnext.idcheckio.sdk.sample.R
import com.ariadnext.idcheckio.sdk.sample.feature.bean.SimpleConfig
import com.ariadnext.idcheckio.sdk.sample.feature.home.HomeFragment
import com.ariadnext.idcheckio.sdk.sample.utils.ImageUtils
import com.ariadnext.idcheckio.sdk.sample.utils.SdkConfig
import com.ariadnext.idcheckio.sdk.sample.utils.ViewUtils
import com.ariadnext.idcheckio.sdk.sample.utils.ViewUtils.addLabelTextView
import com.ariadnext.idcheckio.sdk.sample.utils.ViewUtils.addValueTextView
import com.ariadnext.idcheckio.sdk.sample.utils.exhaustive
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_override.*
import java.io.File

/**
 * An Advanced implementation of the IDCheck.io SDK.
 * Before starting this fragment, you need to take a look at how to activate the SDK in the [HomeFragment].
 * In this fragment we will show you how to completely customize the SDK.
 */
class OverrideFragment : Fragment(), IdcheckioInteractionInterface {
    /**
     * Navigation arguments containing the SDK configuration.
     */
    private val args: OverrideFragmentArgs by navArgs()

    /**
     * Boolean to keep track of the flash status for matching UI state.
     */
    private var isFlashActivated = false

    /**
     * Store the SDK quad coordinates to keep track of it.
     */
    private var quadView: OverrideQuad? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_override, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idcheckioView = SdkConfig.setupSdkByConfig(args.config)
                // The OverrideFragment layout is designed for portrait so we force PORTRAIT orientation here
                .orientation(Orientation.PORTRAIT)
                // The listener which will receive the SDK interaction (such as result, error, ...).
                .listener(this)
                .build()

        // We add the fragment to our view using the child fragment manager and we start it.
        idcheckioView.let {
            childFragmentManager.beginTransaction().replace(R.id.child_container, it).commit()
            it.registerInteraction(IdcheckioInteraction.DATA, null)
            it.registerInteraction(IdcheckioInteraction.UPDATE_CAMERA_LIST, null)
            it.registerInteraction(IdcheckioInteraction.QUAD, null)
            /**
             * By setting a list in the second parameter, you can filter
             * the UI messages you will receive (useful if you want to override only a sublist of messages).
             * In this case, we don't filter anything and pick every messages possible (setting the parameter to null also disable filtering).
             */
            it.registerInteraction(IdcheckioInteraction.UI, IdcheckioUIMsg.values())
            it.start()
        }
    }

    // ---------------------------------------
    // IdcheckioInteractionInterface interface
    // ---------------------------------------

    /**
     * You will receive interaction from the SDK in this method.
     */
    override fun onIdcheckioInteraction(interaction: IdcheckioInteraction, data: Any?) {
        when (interaction) {
            /**
             * The results will always be an [IdcheckioResult] object.
             * It can't be null but it can be empty.
             */
            IdcheckioInteraction.RESULT -> (data as IdcheckioResult).let {
                findNavController().navigate(OverrideFragmentDirections.actionOverrideFragmentToResultFragment(it))
            }
            /**
             * You will receive the errors in an [ErrorMsg] object
             * It can't be null.
             */
            IdcheckioInteraction.ERROR -> {
                Snackbar.make(requireView(), R.string.sdk_error, Snackbar.LENGTH_LONG).show()
                findNavController().popBackStack()
            }
            /**
             * You can filter the cameras to choose a specific one if you need to do so
             * You receive all the available cameras in a [AvailableCamerasMsg]
             */
            IdcheckioInteraction.UPDATE_CAMERA_LIST -> {
                (data as? AvailableCamerasMsg)?.apply {
                    /** Uncomment this sample code for filtering only FRONT facing cameras */
                    //cameras.forEach { it.isEnabled = it.facing == Facing.FRONT }
                }
            }
            /**
             * DATA corresponds to the confirmation popup the user will see to confirm that the capture seems correct to him
             * By overriding this message you can have your own popup design.
             * To send the choice of the user to accept or refuse the results to the sdk, you have access to 2 commands :
             * [EnumCommand.AcceptResults] and [EnumCommand.DeclineResults]. Call the execute() method on one of those
             * commands depending on your user choice.
             */
            IdcheckioInteraction.DATA -> showDataBottomSheet(data as DataMsg)
            /**
             * You can use you own quad design.
             * You will receive a [QuadMsg] object. It contains a [Quad] structure holding the 4 points coordinates of the document
             * recognized in the source image and a boolean to show or hide the quad view.
             *
             * In the case of a selfie, you will receive a square, to show an oval you can stick to the left and right side of the quad and multiply the height by 1.3.
             */
            IdcheckioInteraction.QUAD -> {
                val quadMsg = data as QuadMsg
                if (quadMsg.show) {
                    quadView?.let {
                        it.quad = quadMsg.quad
                    } ?: run {
                        quadView = OverrideQuad(requireContext(), quadMsg.quad, useFaceQuad = args.config == SimpleConfig.SELFIE)
                        override_container.addView(
                                quadView, ConstraintLayout.LayoutParams(
                                ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT
                        )
                        )
                    }
                    quadView?.visibility = View.VISIBLE
                } else {
                    quadView?.visibility = View.GONE
                }

            }
            /**
             * You can see the list of all the message you will receive here in [IdcheckioUIMsg]
             * You will receive a [UIMsg] object. It contains an [IdcheckioUIMsg] and potentially some commands.
             */
            IdcheckioInteraction.UI -> (data as UIMsg).let { uiMsg ->
                when (uiMsg.msg) {
                    /**
                     * Those are all the messages the user will see during the capture
                     * that will help him take a good photo of the desired document.
                     * Those messages are just informational, there is no associated command.
                     */
                    IdcheckioUIMsg.DISPLAY_MOTION -> showInSnackbar(R.string.idcheckio_sdk_scan_motion)
                    IdcheckioUIMsg.IMAGE_BLUR -> showInSnackbar(R.string.idcheckio_sdk_scan_blur)
                    IdcheckioUIMsg.IMAGE_GLARE -> showInSnackbar(R.string.idcheckio_sdk_scan_glare)
                    IdcheckioUIMsg.OCR_FAILED -> showInSnackbar(R.string.idcheckio_sdk_scan_mrz)
                    IdcheckioUIMsg.PICTURE_IN_PROGRESS -> showInSnackbar(R.string.idcheckio_sdk_scan_apperture)
                    IdcheckioUIMsg.SHOW_ZOOM -> showInSnackbar(R.string.idcheckio_sdk_scan_zoom)
                    IdcheckioUIMsg.WRONG_SIDE -> showInSnackbar(R.string.idcheckio_sdk_scan_wrong_side)
                    IdcheckioUIMsg.INVALID_DOCUMENT -> showInSnackbar(R.string.idcheckio_sdk_invalid)
                    IdcheckioUIMsg.INVALID_DOCTYPE -> showInSnackbar(R.string.idcheckio_sdk_error_rejected)
                    IdcheckioUIMsg.SELFIE_QA_DONT_MOVE -> showInSnackbar(R.string.idcheckio_sdk_liveness_qa_be_still)
                    IdcheckioUIMsg.SELFIE_QA_TOO_BLUR -> showInSnackbar(R.string.idcheckio_sdk_liveness_qa_blur)
                    IdcheckioUIMsg.SELFIE_QA_WRONG_EXPOSURE -> showInSnackbar(R.string.idcheckio_sdk_liveness_qa_wrong_exposure)
                    IdcheckioUIMsg.SELFIE_QA_FACE_OVEREXPOSED -> showInSnackbar(R.string.idcheckio_sdk_liveness_qa_exposure_side)
                    IdcheckioUIMsg.SELFIE_QA_UNSTABLE_LIGHT -> showInSnackbar(R.string.idcheckio_sdk_liveness_qa_rolling_intensity)
                    IdcheckioUIMsg.SELFIE_QA_NO_FACE_DETECTED -> showInSnackbar(R.string.idcheckio_sdk_liveness_qa_no_face)
                    IdcheckioUIMsg.SELFIE_QA_FACE_TOO_SMALL -> showInSnackbar(R.string.idcheckio_sdk_liveness_qa_face_small)
                    IdcheckioUIMsg.SELFIE_QA_FACE_TOO_BIG -> showInSnackbar(R.string.idcheckio_sdk_liveness_qa_face_big)
                    IdcheckioUIMsg.SELFIE_QA_NOT_CENTERED -> showInSnackbar(R.string.idcheckio_sdk_liveness_qa_not_centered)
                    IdcheckioUIMsg.CLEAR -> dismissSnackbar()

                    /**
                     * When receiving [IdcheckioUIMsg.SHOW_FLASH], you need to add a button to the view.
                     * You have to keep track of the flash status and depending of the value of the flash,
                     * execute one of the following commands when the user click the button:
                     * [EnumCommand.StartFlash] or [EnumCommand.StopFlash].
                     */
                    IdcheckioUIMsg.SHOW_FLASH -> {
                        fbtn_flash.visibility = View.VISIBLE
                        fbtn_flash.setOnClickListener {
                            if (!isFlashActivated) {
                                uiMsg.commands[EnumCommand.StartFlash]?.execute()
                            } else {
                                uiMsg.commands[EnumCommand.StopFlash]?.execute()
                            }
                            isFlashActivated = !isFlashActivated
                        }
                    }
                    IdcheckioUIMsg.HIDE_FLASH -> fbtn_flash.visibility = View.GONE

                    /**
                     * Use your own loader and hide it when asked.
                     */
                    IdcheckioUIMsg.SHOW_INITIALIZATION_SPINNER -> showInSnackbar(R.string.idcheckio_sdk_common_init)
                    IdcheckioUIMsg.HIDE_INITIALIZATION_SPINNER -> dismissSnackbar()
                    IdcheckioUIMsg.SHOW_LOADING -> showInSnackbar(R.string.idcheckio_sdk_common_loading)
                    IdcheckioUIMsg.HIDE_LOADING -> dismissSnackbar()
                    IdcheckioUIMsg.SHOW_SCANNER_ANIMATION -> showInSnackbar(R.string.idcheckio_sdk_liveness_qa_analysis)
                    IdcheckioUIMsg.HIDE_SCANNER_ANIMATION -> dismissSnackbar()

                    /**
                     * Show/hide the manual capture button.
                     * By default it appears after 10 seconds without finding a eligible document.
                     * You will receive a [EnumCommand.TakePicture] command that you need to attach to the button click.
                     */
                    IdcheckioUIMsg.SHOW_MANUAL_BUTTON -> {
                        fbtn_capture.visibility = View.VISIBLE
                        fbtn_capture.setOnClickListener {
                            uiMsg.commands[EnumCommand.TakePicture]?.execute()
                        }
                    }
                    IdcheckioUIMsg.HIDE_MANUAL_BUTTON -> fbtn_capture.visibility = View.GONE

                    /**
                     * Ask the user to scan the other side of his document.
                     * If the verso is forced, the scan cannot be skipped.
                     * You will receive one or two commands :
                     * [EnumCommand.ScanVerso] is mandatory and always here.
                     * [EnumCommand.SkipVerso] is only received when verso is not forced
                     */
                    IdcheckioUIMsg.PROMPT_SCAN_VERSO_SKIPABLE -> showFlipBottomSheet(uiMsg, true)
                    IdcheckioUIMsg.PROMPT_SCAN_VERSO_NON_SKIPABLE -> showFlipBottomSheet(uiMsg, false)

                    /**
                     * Ask the user to rescan his document because the quality of the first capture wasn't good.
                     * It will only be received on online mode.
                     * You will receive a [EnumCommand.ConfirmRetry] command that you need to attach to the button click.
                     */
                    IdcheckioUIMsg.PROMPT_RETRY -> showRetryBottomSheet(uiMsg)
                    else -> {
                        /* Do nothing */
                    }
                }
            }
            else -> {
                /* Do nothing */
            }
        }
    }

    // -----------------
    // Private functions
    // -----------------

    /**
     * Show a bottom sheet with the results extracted from the card.
     * If the [IdcheckioResult] contains a document, you should show the data extracted from the document.
     * Otherwise you should show the cropped image.
     */
    private fun showDataBottomSheet(dataMsg: DataMsg) {
        if (dataMsg.result.document != null) {
            showDataDocumentBottomSheet(dataMsg)
        } else {
            showCroppedImageBottomSheet(dataMsg)
        }
    }

    /**
     * Show the cropped image in a bottom sheet
     */
    private fun showCroppedImageBottomSheet(dataMsg: DataMsg) {
        val inflater = LayoutInflater.from(requireContext())
        val bottomSheet = inflater.inflate(R.layout.cropped_bottom_sheet, override_container, false)
        val bottomSheetDialog = BottomSheetDialog(requireContext(), com.ariadnext.idcheckio.sdk.R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(bottomSheet)
        val imageResult = dataMsg.result.images[0]
        val titleId = when (imageResult.imageStatus) {
            ImageStatus.QUALITY_ERROR_BLUR -> R.string.idcheckio_sdk_status_blur
            ImageStatus.QUALITY_ERROR_GLARE -> R.string.idcheckio_sdk_status_glare
            else -> R.string.idcheckio_sdk_status_ok
        }
        bottomSheet.findViewById<TextView>(R.id.tv_title).setText(titleId)

        ImageUtils.getBitmapFromUri(requireContext(), Uri.fromFile(File(imageResult.cropped.takeIf { it.isNotEmpty() }
                ?: imageResult.source)))?.let {
            bottomSheet.findViewById<ImageView>(R.id.iv_cropped)?.setImageBitmap(it)
        }
        /** Call the two given commands on button click */
        bottomSheet.findViewById<Button>(R.id.btn_confirm)?.setOnClickListener {
            dataMsg.commands[EnumCommand.AcceptResults]?.execute()
            bottomSheetDialog.setOnDismissListener(null)
            bottomSheetDialog.dismiss()
        }
        bottomSheet.findViewById<Button>(R.id.btn_cancel)?.setOnClickListener {
            dataMsg.commands[EnumCommand.DeclineResults]?.execute()
            bottomSheetDialog.setOnDismissListener(null)
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setOnDismissListener {
            dataMsg.commands[EnumCommand.DeclineResults]?.execute()
        }
        bottomSheetDialog.show()
    }

    /**
     * Show the extracted data in a bottom sheet.
     */
    private fun showDataDocumentBottomSheet(dataMsg: DataMsg) {
        val data = ViewUtils.getDataView(dataMsg.result)
        val inflater = LayoutInflater.from(requireContext())
        val bottomSheet = inflater.inflate(R.layout.data_bottom_sheet, override_container, false)
        val bottomSheetDialog = BottomSheetDialog(requireContext(), com.ariadnext.idcheckio.sdk.R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(bottomSheet)
        /** Change the bottom sheet title with the classified document type */
        dataMsg.result.document?.fields?.get(Document.IdentityDocument.Field.docType)?.let {
            bottomSheet.findViewById<TextView>(R.id.tv_title).text = it.value
        }
        var currentLinearLayout: LinearLayout? = null
        val leftLinearLayout: LinearLayout = bottomSheet.findViewById(R.id.ll_left)
        val rightLinearLayout: LinearLayout = bottomSheet.findViewById(R.id.ll_right)
        for ((index, details) in data.fieldList.withIndex()) {
            /** Switch between 2 linear layout to show the results in two columns */
            if (index % 2 == 0) {
                currentLinearLayout = if (currentLinearLayout != leftLinearLayout) {
                    leftLinearLayout
                } else {
                    rightLinearLayout
                }
            }
            /** Show every extracted results */
            when (details.fieldType) {
                DataView.FieldType.LABEL -> addLabelTextView(requireContext(), currentLinearLayout!!, details.value as Int)
                DataView.FieldType.VALUE -> addValueTextView(requireContext(), currentLinearLayout!!, details.value.toString())
            }.exhaustive
        }
        /** Call the two given commands on button click */
        bottomSheet.findViewById<Button>(R.id.btn_confirm)?.setOnClickListener {
            dataMsg.commands[EnumCommand.AcceptResults]?.execute()
            bottomSheetDialog.setOnDismissListener(null)
            bottomSheetDialog.dismiss()
        }
        bottomSheet.findViewById<Button>(R.id.btn_cancel)?.setOnClickListener {
            dataMsg.commands[EnumCommand.DeclineResults]?.execute()
            bottomSheetDialog.setOnDismissListener(null)
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setOnDismissListener {
            dataMsg.commands[EnumCommand.DeclineResults]?.execute()
        }
        bottomSheetDialog.show()
    }

    /**
     * Show the popup asking to scan the second side
     */
    private fun showFlipBottomSheet(uiMsg: UIMsg, isSkipEnable: Boolean) {
        val inflater = LayoutInflater.from(requireContext())
        val bottomSheet = inflater.inflate(R.layout.flip_bottom_sheet, override_container, false)
        val bottomSheetDialog = BottomSheetDialog(requireContext(), com.ariadnext.idcheckio.sdk.R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(bottomSheet)
        if (!isSkipEnable) {
            bottomSheet.findViewById<Button>(R.id.btn_cancel).visibility = View.GONE
            bottomSheetDialog.setCancelable(false)
        }
        bottomSheet.findViewById<Button>(R.id.btn_confirm)?.setOnClickListener {
            uiMsg.commands[EnumCommand.ScanVerso]?.execute()
            bottomSheetDialog.setOnDismissListener(null)
            bottomSheetDialog.dismiss()
        }
        bottomSheet.findViewById<Button>(R.id.btn_cancel)?.setOnClickListener {
            uiMsg.commands[EnumCommand.SkipVerso]?.execute()
            bottomSheetDialog.setOnDismissListener(null)
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setOnDismissListener {
            uiMsg.commands[EnumCommand.SkipVerso]?.execute()
        }
        bottomSheetDialog.show()
    }

    /**
     * Show the popup asking to rescan the document
     */
    private fun showRetryBottomSheet(uiMsg: UIMsg) {
        val inflater = LayoutInflater.from(requireContext())
        val bottomSheet = inflater.inflate(R.layout.retry_bottom_sheet, override_container, false)
        val bottomSheetDialog = BottomSheetDialog(requireContext(), com.ariadnext.idcheckio.sdk.R.style.BottomSheetDialogTheme)
        bottomSheetDialog.setContentView(bottomSheet)
        bottomSheet.findViewById<Button>(R.id.btn_confirm)?.setOnClickListener {
            uiMsg.commands[EnumCommand.ConfirmRetry]?.execute()
            bottomSheetDialog.setOnDismissListener(null)
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setOnDismissListener {
            uiMsg.commands[EnumCommand.ConfirmRetry]?.execute()
        }
        bottomSheetDialog.show()
    }

    /**
     * Show the help messages in a custom snackbar
     */
    private fun showInSnackbar(message: Int) {
        snackbar_group.visibility = View.VISIBLE
        snackbar_text.setText(message)
    }

    /**
     * Hide the snackbar
     */
    private fun dismissSnackbar() {
        snackbar_group.visibility = View.GONE
    }
}
