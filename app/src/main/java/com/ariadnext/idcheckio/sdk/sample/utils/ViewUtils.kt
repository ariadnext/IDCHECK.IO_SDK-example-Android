package com.ariadnext.idcheckio.sdk.sample.utils

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import com.ariadnext.idcheckio.sdk.interfaces.result.Document
import com.ariadnext.idcheckio.sdk.interfaces.result.IField
import com.ariadnext.idcheckio.sdk.interfaces.result.IdcheckioResult
import com.ariadnext.idcheckio.sdk.sample.R
import com.ariadnext.idcheckio.sdk.sample.feature.overridecapture.DataView
import kotlin.math.roundToInt

object ViewUtils {

    fun addLabelTextView(context: Context, ll: LinearLayout, value: Int) {
        val textView = TextView(context)
        textView.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimension(R.dimen.small_text_size)
        )
        textView.setText(value)
        textView.setTextColor(context.resources.getColorCompat(R.color.gray_label))
        textView.setPadding(
            context.resources.getDimension(R.dimen.data_label_left_margin).roundToInt(),
            0,
            context.resources.getDimension(R.dimen.data_label_right_margin).roundToInt(),
            0
        )
        val lllp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        ll.addView(textView, lllp)
    }

    fun addValueTextView(context: Context, ll: LinearLayout, value: String) {
        val textView = TextView(context)
        textView.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimension(R.dimen.normal_text_size)
        )
        textView.text = value
        textView.setTypeface(textView.typeface, Typeface.BOLD)
        textView.setTextColor(context.resources.getColorCompat(R.color.black))
        textView.setPadding(
            context.resources.getDimension(R.dimen.data_value_left_margin).roundToInt(),
            0,
            context.resources.getDimension(R.dimen.data_value_right_margin).roundToInt(),
            context.resources.getDimension(R.dimen.data_value_bottom_margin).roundToInt()
        )
        val lllp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        ll.addView(textView, lllp)
    }

    fun getDataView(idcheckioResult: IdcheckioResult): DataView {
        val dataView = DataView()
        idcheckioResult.document?.let { doc ->
            val dataList = ArrayList<DataView.Field>()
            for (field in doc.fields) {
                getStringForLabel(field.key)?.let {
                    dataList.add(
                        DataView.Field(
                            value = it,
                            fieldType = DataView.FieldType.LABEL
                        )
                    )
                    dataList.add(
                        DataView.Field(
                            value = field.value.value,
                            fieldType = DataView.FieldType.VALUE
                        )
                    )
                }
            }
            dataView.fieldList = dataList
        }
        return dataView
    }

    private fun getStringForLabel(field: IField): Int? {
        return when (field) {
            Document.IdentityDocument.Field.birthDate -> R.string.data_birthdate
            Document.IdentityDocument.Field.documentNumber, Document.VehicleRegistrationDocument.Field.documentNumber -> R.string.data_documentNumber
            Document.IdentityDocument.Field.emitCountry, Document.VehicleRegistrationDocument.Field.emitCountry -> R.string.data_emitCountry
            Document.IdentityDocument.Field.emitDate -> R.string.data_emitDate
            Document.IdentityDocument.Field.expirationDate -> R.string.data_expirationDate
            Document.IdentityDocument.Field.firstNames -> R.string.data_firstNames
            Document.IdentityDocument.Field.gender -> R.string.data_gender
            Document.IdentityDocument.Field.lastNames -> R.string.data_lastNames
            Document.IdentityDocument.Field.nationality -> R.string.data_nationality
            Document.IdentityDocument.Field.personalNumber -> R.string.data_personalNumber
            Document.IdentityDocument.Field.emitDepartement -> R.string.data_emitDepartement
            Document.VehicleRegistrationDocument.Field.firstRegistrationDate -> R.string.data_firstRegistrationDate
            Document.VehicleRegistrationDocument.Field.make -> R.string.data_make
            Document.VehicleRegistrationDocument.Field.model -> R.string.data_model
            Document.VehicleRegistrationDocument.Field.registrationNumber -> R.string.data_registrationNumber
            Document.VehicleRegistrationDocument.Field.vehicleNumber -> R.string.data_vehicleNumber
            else -> return null
        }
    }

}