package com.ariadnext.idcheckioexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ariadnext.android.smartsdk.bean.enums.AXTSdkParameters;
import com.ariadnext.android.smartsdk.exception.CaptureApiException;
import com.ariadnext.android.smartsdk.interfaces.AXTCaptureInterface;
import com.ariadnext.android.smartsdk.interfaces.AXTCaptureInterfaceCallback;
import com.ariadnext.android.smartsdk.interfaces.bean.AXTDataExtractionRequirement;
import com.ariadnext.android.smartsdk.interfaces.bean.AXTDocumentType;
import com.ariadnext.android.smartsdk.interfaces.bean.AXTSdkInit;
import com.ariadnext.android.smartsdk.interfaces.bean.AXTSdkParams;
import com.ariadnext.android.smartsdk.interfaces.bean.AXTSdkResult;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements AXTCaptureInterfaceCallback {
    private final static int SDK_REQUEST_CODE = 10;

    private boolean isInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // You need to call the init method at the beginning of the activity
        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        final boolean permissionsAllowed = AXTCaptureInterface.INSTANCE.verifyPermissions(requestCode, permissions, grantResults);
        if (permissionsAllowed) {
            try {
                this.init();
            } catch (final Exception ex) {
                Log.e("SMARTSDK-CLIENT", "An exception occured during SmartSdk initialization.", ex);
                ResultUtils.INSTANCE.showSnackBarWithMsg(getString(R.string.error_init), findViewById(R.id.main_activity));
            }
        } else {
            ResultUtils.INSTANCE.showSnackBarWithMsg(getString(R.string.error_permissions), findViewById(R.id.main_activity));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SDK_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    // result contains every information that will be return by the SDK
                    final AXTSdkResult result = AXTCaptureInterface.INSTANCE.getResultImageFromCapture(data);

                    ResultUtils.INSTANCE.updateResultState(this, result);
                } catch (final CaptureApiException ex) {
                    switch (ex.getCodeType()) {
                        case LICENSE_SDK_ERROR:
                            ResultUtils.INSTANCE.showSnackBarWithMsg(getString(R.string.error_not_initialized), findViewById(R.id.main_activity));
                            break;
                        case UNSUPPORTED_FEATURE:
                            ResultUtils.INSTANCE.showSnackBarWithMsg(getString(R.string.error_arch_support), findViewById(R.id.main_activity));
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    /**
     * This method initialize the sdk, you need to call it before trying to capture anything with the sdk
     */
    public void init() {
        try {
            AXTSdkInit sdkInit = new AXTSdkInit("licence");
            sdkInit.setUseImeiForActivation(false);
            AXTCaptureInterface.INSTANCE.initCaptureSdk(this, sdkInit, this);
        } catch (CaptureApiException e) {
            Log.e("SMARTSDK-CLIENT", "An exception occured during SmartSdk initialization.", e);
            ResultUtils.INSTANCE.showSnackBarWithMsg(getString(R.string.error_init), findViewById(R.id.main_activity));
        }
    }


    /**
     * This method starts the capture with the settings you give her in entry
     */
    @OnClick(R.id.capture)
    public void capture() {
        if (this.isInitialized) {
            AXTSdkParams params = getDefaultSDKParams();
            try {
                final Intent smartSdk = AXTCaptureInterface.INSTANCE.getIntentCapture(this.getApplicationContext(), params);
                startActivityForResult(smartSdk, SDK_REQUEST_CODE);
            } catch (CaptureApiException e) {
                ResultUtils.INSTANCE.showSnackBarWithMsg(getString(R.string.error_start), this.findViewById(R.id.main_activity));
            }
        } else {
            ResultUtils.INSTANCE.showSnackBarWithMsg(getString(R.string.error_init), findViewById(R.id.main_activity));
        }
    }

    /**
     * Define the settings you want to use here
     *
     * @return settings of the sdk
     */
    public static AXTSdkParams getDefaultSDKParams() {
        AXTSdkParams params = new AXTSdkParams();
        params.setDocType(AXTDocumentType.ID);
        params.addParameters(AXTSdkParameters.DISPLAY_CAPTURE, true);
        params.addParameters(AXTSdkParameters.EXTRACT_DATA, true);
        params.addParameters(AXTSdkParameters.SCAN_RECTO_VERSO, true);
        params.addParameters(AXTSdkParameters.DATA_EXTRACTION_REQUIREMENT, AXTDataExtractionRequirement.MRZ_FOUND);
        params.addParameters(AXTSdkParameters.READ_RFID, true);
        params.addParameters(AXTSdkParameters.USE_FRONT_CAMERA, false);
        return params;
    }

    @Override
    public void onInitSuccess() {
        this.isInitialized = true;
    }

    @Override
    public void onInitError() {
        this.isInitialized = false;
    }
}
