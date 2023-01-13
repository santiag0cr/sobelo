package com.ultimate.ultimatesophos.data.models.response

import com.google.gson.annotations.SerializedName

data class DocumentDetailModel(
    @SerializedName("Ciudad")
    val city: String,
    @SerializedName("Fecha")
    val date: String,
    @SerializedName("TipoAdjunto")
    val attachmentType: String,
    @SerializedName("Nombre")
    val name: String,
    @SerializedName("Apellido")
    val lastName: String,
    @SerializedName("Identificacion")
    val identification: String,
    @SerializedName("IdRegistro")
    val id: String,
    @SerializedName("TipoId")
    val idType: String,
    @SerializedName("Correo")
    val email: String,
    @SerializedName("Adjunto")
    val attachment: String
)
