package com.ultimate.ultimatesophos.data.models.response

import com.google.gson.annotations.SerializedName

data class DocumentInfoModel(
    @SerializedName("IdRegistro")
    val id: String,
    @SerializedName("Fecha")
    val date: String,
    @SerializedName("TipoAdjunto")
    val attachmentType: String,
    @SerializedName("Nombre")
    val name: String,
    @SerializedName("Apellido")
    val lastName: String
)
