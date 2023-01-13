package com.ultimate.ultimatesophos.data.models.request

import com.google.gson.annotations.SerializedName

data class PutDocumentsRequestEntity(
    @SerializedName("TipoId") var idType: String,
    @SerializedName("Identificacion") var identification: String,
    @SerializedName("Nombre") var name: String,
    @SerializedName("Apellido") var lastName: String,
    @SerializedName("Ciudad") var city: String,
    @SerializedName("Correo") var email: String,
    @SerializedName("TipoAdjunto") var attachmentType: String,
    @SerializedName("Adjunto") var attachment: String
)