package com.ariadnext.idcheckiosdk.sample

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ariadnext.idcheckio.sdk.bean.CISContext
import com.ariadnext.idcheckio.sdk.bean.DocumentType
import com.ariadnext.idcheckio.sdk.bean.SdkEnvironment
import com.ariadnext.idcheckio.sdk.component.Idcheckio
import com.ariadnext.idcheckio.sdk.interfaces.ErrorMsg
import com.ariadnext.idcheckio.sdk.interfaces.IdcheckioCallback
import com.ariadnext.idcheckio.sdk.interfaces.InitStatus
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), IdcheckioCallback {

    companion object {
        const val TAG = "MainActivity"

        // Request code used to launch CaptureActivity and retrieve it's scanning result
        const val START_SCANNING_REQUEST_CODE = 1

        // Request code regarding SDK's specific permissions
        private val PERMISSION_REQUEST_CODE = 111
    }

    private var isOnline = true
    private var docType: DocumentType = DocumentType.ID
    // The CISContext data for matching online liveness with reference ID
    private var cisContext: CISContext? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Prepare the SDK (pre-load assets and activate licence)
        setupSDK()
        setupUI()
    }

    override fun onResume() {
        super.onResume()
        checkPermissions()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == START_SCANNING_REQUEST_CODE) {

            // Store Online result for next online session (liveness)
            cisContext = CISContext(
                data?.extras?.getString(CaptureActivity.PARAM_RESULT_FOLDER_KEY),
                data?.extras?.getString(CaptureActivity.PARAM_RESULT_TASK_KEY),
                data?.extras?.getString(CaptureActivity.PARAM_RESULT_DOCUMENT_KEY)
            )

            // Handle Document result
            when (docType) {
                DocumentType.ID -> {
                    Toast.makeText(
                        this,
                        "ID Capture OK ${data?.extras?.getString(CaptureActivity.PARAM_RESULT_NAME_KEY) ?: ""}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    Toast.makeText(
                        this, "Liveness Session Complete",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // Check if all required permissions are granted
                val permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                if (!(permissionCamera == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Please grant Camera access permission to SDK", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Private functions
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private fun setupUI() {
        tv_main_sdk_version.text = resources.getString(R.string.main_sdk_version, Idcheckio.getSdkVersion())

        setupListeners()
        updateUI()

        btn_main_scan.setOnClickListener {
            // Navigate to the scanning activity
            startActivityForResult(prepareCapture(), START_SCANNING_REQUEST_CODE)
        }

        tv_main_clear_context.setOnClickListener {
            cisContext = null
            Toast.makeText(this, "CIS Context cleared", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSDK() {
        // Pre load IDCheck.io SDK assets (do it at the start of your application)
        Idcheckio.preload(this, true)
    }

    private fun checkPermissions() {
        // Ensure the user gave CAMERA and RECORD_AUDIO permission to the app
        val permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//        val permissionMicrophone = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)

        val listPermissionsNeeded = ArrayList<String>()
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
 //       if (permissionMicrophone != PackageManager.PERMISSION_GRANTED) {
 //           listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO)
 //       }
        // Ask user for missing permissions if needed
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), PERMISSION_REQUEST_CODE)
        } else {
            activateSDK()
        }
    }

    private fun activateSDK() {
        if (Idcheckio.initStatus != InitStatus.SUCCESS) {
            // Activate SDK (call this when the users enters the onboarding sequence and you need the SDK for scanning)
            Idcheckio.activate("licence", this, this, true, environment = SdkEnvironment.DEMO)
        } else {
            btn_main_scan.isEnabled = true
        }
    }

    private fun updateUI() {
        // Refresh UI based on data
        sw_main_document_type_id.isChecked = docType == DocumentType.ID
        sw_main_document_type_liveness.isChecked = docType == DocumentType.LIVENESS
        sw_main_online_mode.isChecked = isOnline
    }

    private fun setupListeners() {
        sw_main_document_type_id.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                docType = DocumentType.ID
            }
            updateUI()
        }

        sw_main_document_type_liveness.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                docType = DocumentType.LIVENESS
            }
            updateUI()
        }

        sw_main_online_mode.setOnCheckedChangeListener { _, isChecked ->
            isOnline = isChecked
        }
    }

    private fun prepareCapture(): Intent {
        val captureIntent = Intent(this, CaptureActivity::class.java)
            .putExtra(CaptureActivity.PARAM_DOCTYPE_KEY, docType.name)
            .putExtra(CaptureActivity.PARAM_ONLINE_KEY, isOnline)

        cisContext?.apply {
            captureIntent.putExtra(CaptureActivity.PARAM_FOLDER_KEY, folderUid)
            captureIntent.putExtra(CaptureActivity.PARAM_DOCUMENT_KEY, referenceDocUid)
            captureIntent.putExtra(CaptureActivity.PARAM_TASK_KEY, referenceTaskUid)
        }

        Log.d(MainActivity::class.java.simpleName, "prepareCapture -> ${captureIntent.toUri(0)}")

        return captureIntent
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // INTERFACE : IdcheckioCallback
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Handle SDK Initialization success or errors
    override fun onInitEnd(success: Boolean, error: ErrorMsg?) {
        if (!success) {
            val message = "Error while initializing IDCheck.io SDK :"
            Toast.makeText(
                this,
                error?.let { errorMsg ->
                    "$message${errorMsg.code?.let { "code=$it" } ?: ""} message=${errorMsg.message}"
                } ?: "$message Unknown error"
                ,
                Toast.LENGTH_LONG
            ).show()
        } else {
            Log.d(TAG, "onInitEnd -> Success!")
        }
        btn_main_scan.isEnabled = success
    }

}
