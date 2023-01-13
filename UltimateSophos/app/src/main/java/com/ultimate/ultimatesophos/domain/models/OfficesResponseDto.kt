package com.ultimate.ultimatesophos.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OfficesResponseDto(
    var items: List<OfficeDto> = emptyList(),
    var count: Int?,
    var scannedCount: Int?
) : Parcelable

@Parcelize
data class OfficeDto(
    var city: String,
    var longitude: String,
    var id: Int,
    var latitude: String,
    var name: String
) : Parcelable
