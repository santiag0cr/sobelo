package com.ultimate.ultimatesophos.data.models.response

import com.google.gson.annotations.SerializedName

data class DocumentDetailResponseModel(
    @SerializedName("Items")
    val items: List<DocumentDetailModel> = emptyList(),
    @SerializedName("Count")
    val count: Int? = null,
    @SerializedName("ScannedCount")
    val scannedCount: Int? = null
)
