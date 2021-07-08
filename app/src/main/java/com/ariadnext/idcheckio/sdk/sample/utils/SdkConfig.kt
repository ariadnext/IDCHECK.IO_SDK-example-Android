package com.ariadnext.idcheckio.sdk.sample.utils

import com.ariadnext.idcheckio.sdk.bean.*
import com.ariadnext.idcheckio.sdk.component.IdcheckioView
import com.ariadnext.idcheckio.sdk.sample.feature.bean.SimpleConfig

object SdkConfig {

    /**
     * Get the parameter associated with the config you choose
     */
    fun setupSdkByConfig(config: SimpleConfig): IdcheckioView.Builder {
        return when (config) {
            SimpleConfig.ID -> setupSdkForID()
            SimpleConfig.SELFIE -> setupSdkForSelfie()
            SimpleConfig.IBAN -> setupSdkForIban()
            SimpleConfig.VEHICLE_REGISTRATION -> setupSdkForVehicleRegistration()
            SimpleConfig.ADDRESS_PROOF -> setupSdkForAddressProof()
            SimpleConfig.FRENCH_HEALTH_CARD -> setupSdkForFrenchHealthCard()
        }
    }

    /**
     * Best practice parameter to capture a french ID Card
     */
    private fun setupSdkForID(): IdcheckioView.Builder {
        return IdcheckioView.Builder()
            .docType(DocumentType.ID)
            .scanBothSides(Forceable.ENABLED)
            .confirmType(ConfirmationType.DATA_OR_PICTURE)
            .sideOneExtraction(
                Extraction(
                    codeline = DataRequirement.VALID,
                    face = FaceDetection.ENABLED
                )
            )
            .sideTwoExtraction(
                Extraction(
                    codeline = DataRequirement.REJECT,
                    face = FaceDetection.DISABLED
                )
            )
            .orientation(Orientation.PORTRAIT)
            .readEmrtd(true)
    }

    /**
     * Best practice parameter to capture a vehicle registration
     */
    private fun setupSdkForVehicleRegistration(): IdcheckioView.Builder {
        return IdcheckioView.Builder()
            .docType(DocumentType.VEHICLE_REGISTRATION)
            .confirmType(ConfirmationType.DATA_OR_PICTURE)
            .sideOneExtraction(
                Extraction(
                    codeline = DataRequirement.VALID,
                    face = FaceDetection.DISABLED
                )
            )
            .orientation(Orientation.PORTRAIT)
    }

    /**
     * Best practice parameter to capture a french health Card
     */
    private fun setupSdkForFrenchHealthCard(): IdcheckioView.Builder {
        return IdcheckioView.Builder()
            .docType(DocumentType.FRENCH_HEALTH_CARD)
            .confirmType(ConfirmationType.DATA_OR_PICTURE)
            .orientation(Orientation.PORTRAIT)
    }

    /**
     * Best practice parameter to make a liveness session
     */
    fun setupSdkForLiveness(): IdcheckioView.Builder {
        return IdcheckioView.Builder()
            .docType(DocumentType.LIVENESS)
            .orientation(Orientation.PORTRAIT)
            .confirmAbort(confirmAbort = true)
    }

    /**
     * Best practice parameter to take a selfie
     */
    private fun setupSdkForSelfie(): IdcheckioView.Builder {
        return IdcheckioView.Builder()
            .docType(DocumentType.SELFIE)
            .orientation(Orientation.PORTRAIT)
            .confirmType(ConfirmationType.DATA_OR_PICTURE)
            .useHd(true)
    }

    /**
     * Best practice parameter to capture an Address proof
     */
    private fun setupSdkForAddressProof(): IdcheckioView.Builder {
        return IdcheckioView.Builder()
            .docType(DocumentType.A4)
            .orientation(Orientation.PORTRAIT)
            .confirmType(ConfirmationType.DATA_OR_PICTURE)
            .useHd(true)
    }

    /**
     * Best practice parameter to capture an IBAN
     */
    private fun setupSdkForIban(): IdcheckioView.Builder {
        return IdcheckioView.Builder()
            .docType(DocumentType.PHOTO)
            .orientation(Orientation.PORTRAIT)
            .confirmType(ConfirmationType.DATA_OR_PICTURE)
            .adjustCrop(true)
    }
}