package com.ariadnext.idcheckio.sdk.sample.feature.bean

/** Class used to store the data extracted from the SDK */
class DataView {
    var fieldList: List<Field> = emptyList()

    data class Field(
        val value: Any,
        val fieldType: FieldType
    )

    enum class FieldType {
        LABEL, VALUE
    }
}