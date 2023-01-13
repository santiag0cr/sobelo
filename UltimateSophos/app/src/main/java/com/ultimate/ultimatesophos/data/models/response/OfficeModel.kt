package com.ultimate.ultimatesophos.data.models.response

import com.google.gson.annotations.SerializedName

data class OfficeModel(
    @SerializedName("Ciudad")
    val city: String,
    @SerializedName("Longitud")
    val longitude: String,
    @SerializedName("IdOficina")
    val id: Int,
    @SerializedName("Latitud")
    val latitude: String,
    @SerializedName("Nombre")
    val name: String
)
