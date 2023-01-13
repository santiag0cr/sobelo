package com.ultimate.ultimatesophos.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DocumentDetailDto(
    val itemList: List<DocumentDetailedInfoDto> = emptyList(),
    val count: Int,
    val scannedCount: Int
) : Parcelable

@Parcelize
data class DocumentDetailedInfoDto(
    val id: String,
    val date: String,
    val idType: String,
    val identification: String,
    val name: String,
    val lastName: String,
    val city: String,
    val email: String,
    val attachmentType: String,
    val attachment: String
) : Parcelable
