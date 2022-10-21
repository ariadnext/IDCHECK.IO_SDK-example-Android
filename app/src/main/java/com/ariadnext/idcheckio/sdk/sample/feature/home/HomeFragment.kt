package com.ariadnext.idcheckio.sdk.sample.feature.home

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ariadnext.idcheckio.sdk.bean.*
import com.ariadnext.idcheckio.sdk.component.Idcheckio
import com.ariadnext.idcheckio.sdk.interfaces.*
import com.ariadnext.idcheckio.sdk.interfaces.result.IdcheckioResult
import com.ariadnext.idcheckio.sdk.sample.BuildConfig
import com.ariadnext.idcheckio.sdk.sample.R
import com.ariadnext.idcheckio.sdk.sample.feature.bean.SimpleConfig
import com.ariadnext.idcheckio.sdk.sample.feature.home.HomeAdapter.Companion.ADVANCED_CAPTURE
import com.ariadnext.idcheckio.sdk.sample.feature.home.HomeAdapter.Companion.ANALYZE
import com.ariadnext.idcheckio.sdk.sample.feature.home.HomeAdapter.Companion.ONLINE_FLOW
import com.ariadnext.idcheckio.sdk.sample.feature.home.HomeAdapter.Companion.SIMPLE_CAPTURE
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * This fragment handle SDK permissions and activation.
 * It will present you the different examples we offer.
 */
class HomeFragment : Fragment(), IdcheckioCallback {

    companion object {
        /**
         * Request code for the intent to retrieve images from file system
         */
        private const val PICK_IMAGE_REQUEST_CODE = 2563

    }

    /**
     * We keep track of the URI for the image files we want to analyze
     */
    private var side1ImageUri: Uri? = null
    private var side2ImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activate()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vp_home.adapter = HomeAdapter()
        TabLayoutMediator(tl_home, vp_home) { _, _ -> }.attach()

        btn_home.setOnClickListener {
            // Before moving to the next fragment, we must ensure the SDK is successfully activated.
            if (Idcheckio.initStatus == InitStatus.SUCCESS || Idcheckio.initStatus == InitStatus.WAITING_FOR_RESTART) {
                when (vp_home.currentItem) {
                    ONLINE_FLOW -> findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToIdFragment())
                    SIMPLE_CAPTURE -> findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToSimpleCaptureFragment(
                                    getConfig()
                            )
                    )
                    ADVANCED_CAPTURE -> findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToOverrideFragment(getConfig()))
                    ANALYZE -> createImageChooser()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            PICK_IMAGE_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val uri = data?.data
                    uri ?: return
                    if (side1ImageUri == null) {
                        side1ImageUri = uri
                        askSecondSide()
                    } else {
                        side2ImageUri = uri
                        doAnalyze()
                    }
                }
            }
        }
    }

    // ---------------------------
    // IdcheckioCallback interface
    // ---------------------------

    /**
     * This is the callback interface implementation. We're notified though this function once SDK activation is done.
     * @param success : a boolean flag to check if the SDK activation was successful or not
     * @param error : an eventual error message if the activation failed (invalid licence, no internet access...)
     */
    override fun onInitEnd(success: Boolean, error: ErrorMsg?) {
        if (!success) {
            Snackbar.make(requireView(), R.string.home_init_failed, Snackbar.LENGTH_INDEFINITE).apply {
                setAction(R.string.common_retry) {
                    activate()
                    dismiss()
                }
            }.show()
        }
    }

    // -----------------
    // Private functions
    // -----------------

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
            // The Context is needed to initialize the SDK components.
            context = requireContext(),
            // Set this flag to true if you need to check MRZ from the SDK (i.e. if using one of the following parameter in the SDK : sideOneExtraction, sideTwoExtraction)
            extractData = true
        )
    }

    /**
     * Configure the SDK for analyzing a picture instead of using live camera feed
     */
    private fun doAnalyze() {
        val localSide1Analyze = side1ImageUri ?: return /* Should not happen */

        /** Here, we define an object implementing the [IdcheckioInteractionInterface] that will be used
         * as a callback for the SDK interaction on this analyze session. We only focus here on capture result or error. */
        val idcheckioInteractionInterface = object : IdcheckioInteractionInterface {
            override fun onIdcheckioInteraction(interaction: IdcheckioInteraction, data: Any?) {
                loading_group.visibility = View.GONE
                when (interaction) {
                    IdcheckioInteraction.RESULT -> findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToResultFragment(
                                    data as IdcheckioResult
                            )
                    )
                    IdcheckioInteraction.ERROR -> Snackbar.make(requireView(), R.string.sdk_error, Snackbar.LENGTH_LONG).show()
                    else -> { /* Do nothing */
                    }
                }
            }
        }

        loading_group.visibility = View.VISIBLE
        /**
         * You can call this method on the UI Thread, it's not blocking
         * If you want to use the analyze with the online mode activated, you can
         * set the onlineContext parameter with the one given by the SDK as a result in your last capture.
         * If it's your first document, set it to null.
         */
        Idcheckio.analyze(
                // We need a context to retrieve resources.
                context = requireContext(),
                // The parameters you want for your image analyze
                captureParams = CaptureParams().apply {
                    documentType = DocumentType.ID
                    addParameter(EnumParameters.SIDE_1_EXTRACTION, Extraction(DataRequirement.ANY, FaceDetection.ENABLED))
                    addParameter(EnumParameters.SIDE_2_EXTRACTION, Extraction(DataRequirement.ANY, FaceDetection.ENABLED))
                    /**
                     * Online mode only
                     * If you already have a folderUid and if you want to use it, you can set it in the first capture of the SDK in the [OnlineConfig]
                     * The created document will be added to this folder.
                     * If you didn't set it the SDK will create a folder and return you the uid at the end of the capture in the [OnlineContext].
                     * You only have to set it in the first capture, it will store in the [OnlineContext] for the next capture.
                     */
                    //addParameter(EnumParameters.ONLINE_CONFIG, OnlineConfig(folderUid = "myFolderUid"))
                },
                // The first side of your document is mandatory.
                side1ToUpload = localSide1Analyze,
                // The second side of your document.
                side2ToUpload = side2ImageUri,
                // Set to true if you want your image to be automatically sent to the CIS by the SDK.
                isOnline = false,
                // The callback where you want to receive the analysis results.
                callback = idcheckioInteractionInterface,
                // You can provide an online context if needed.
                onlineContext = null
        )
        // Reset URIs once analyze has been called.
        side1ImageUri = null
        side2ImageUri = null
    }

    private fun getConfig(): SimpleConfig {
        return vp_home.findViewById<Spinner>(R.id.spinner).selectedItem as SimpleConfig
    }

    /**
     * Create an intent to choose a image
     */
    private fun createImageChooser() {
        val intent = Intent()
        // Show only images, not videos or anything else.
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        // Always show the chooser (if there are multiple options available).
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_CODE)
    }

    /**
     * Ask the user if he want to choose a second image
     */
    private fun askSecondSide() {
        val dialog = AlertDialog.Builder(context)
                .setTitle("Do you want to choose a second image ?")
                .setCancelable(true)
                .setPositiveButton("Yes") { dialogInterface, _ ->
                    createImageChooser()
                    dialogInterface.dismiss()
                }
                .setNegativeButton("No") { dialogInterface, _ ->
                    doAnalyze()
                    dialogInterface.dismiss()
                }
        dialog.show()
    }
}
