package com.ultimate.ultimatesophos.data.models.response

import com.google.gson.annotations.SerializedName

data class DocumentsListResponseModel(
    @SerializedName("Items")
    val items: List<DocumentInfoModel> = emptyList(),
    @SerializedName("Count")
    val count: Int? = null,
    @SerializedName("ScannedCount")
    val scannedCount: Int? = null
)
