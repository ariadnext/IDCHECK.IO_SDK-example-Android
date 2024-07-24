package com.ariadnext.idcheckio.sdk.sample.utils

import com.ariadnext.idcheckio.sdk.bean.ConfirmationType
import com.ariadnext.idcheckio.sdk.bean.DocumentType
import com.ariadnext.idcheckio.sdk.bean.OnlineConfig
import com.ariadnext.idcheckio.sdk.bean.Orientation
import com.ariadnext.idcheckio.sdk.component.IdcheckioView
import com.ariadnext.idcheckio.sdk.sample.feature.bean.SimpleConfig

class SdkConfig {

    companion object {
        /**
         * Get the parameter associated with the config you choose
         */
        fun setupSdkByConfig(config: SimpleConfig): IdcheckioView.Builder {
            return when (config) {
                SimpleConfig.ID -> setupSdkForId()
                SimpleConfig.SELFIE -> setupSdkForSelfie()
                SimpleConfig.IBAN -> setupSdkForIban()
                SimpleConfig.ADDRESS_PROOF -> setupSdkForAddressProof()
                SimpleConfig.FRENCH_HEALTH_CARD -> setupSdkForFrenchHealthCard()
                SimpleConfig.LIVENESS -> setupSdkForLiveness()
            }
        }

        /**
         * Best practice parameter to capture a french health Card
         */
        private fun setupSdkForFrenchHealthCard(): IdcheckioView.Builder {
            return IdcheckioView.Builder()
                .docType(DocumentType.FRENCH_HEALTH_CARD)
                .orientation(Orientation.PORTRAIT)
                .confirmType(ConfirmationType.DATA_OR_PICTURE)
        }

        /**
         * Best practice parameter to make a liveness session
         */
        private fun setupSdkForLiveness(): IdcheckioView.Builder {
            return IdcheckioView.Builder()
                .docType(DocumentType.LIVENESS)
                .orientation(Orientation.PORTRAIT)
                .confirmAbort(confirmAbort = true)
        }

        /**
         * Best practice parameter to make an online session
         */
        private fun setupSdkForId(): IdcheckioView.Builder {
            return IdcheckioView.Builder()
                .docType(DocumentType.ID)
                .orientation(Orientation.PORTRAIT)
                .onlineConfig(
                    OnlineConfig(
                        /** Set this Identity document as the reference document for your future liveness session. */
                        isReferenceDocument = true
                    )
                )
        }

        /**
         * Best practice parameter to take a selfie
         */
        private fun setupSdkForSelfie(): IdcheckioView.Builder {
            return IdcheckioView.Builder()
                .docType(DocumentType.SELFIE)
                .orientation(Orientation.PORTRAIT)
                .confirmType(ConfirmationType.DATA_OR_PICTURE)
        }

        /**
         * Best practice parameter to capture an Address proof
         */
        private fun setupSdkForAddressProof(): IdcheckioView.Builder {
            return IdcheckioView.Builder()
                .docType(DocumentType.A4)
                .orientation(Orientation.PORTRAIT)
                .confirmType(ConfirmationType.DATA_OR_PICTURE)
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
}