package com.ultimate.ultimatesophos.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DocumentsListDto(
    val itemList: List<DocumentGeneralInfoDto> = emptyList(),
    val count: Int,
    val scannedCount: Int
) : Parcelable

@Parcelize
data class DocumentGeneralInfoDto(
    val id: String,
    val date: String,
    val attachmentType: String,
    val name: String,
    val lastName: String
) : Parcelable
