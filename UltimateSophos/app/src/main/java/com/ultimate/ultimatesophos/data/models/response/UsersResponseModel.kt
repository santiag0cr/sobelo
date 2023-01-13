package com.ultimate.ultimatesophos.data.models.response

import com.google.gson.annotations.SerializedName

data class UsersResponseModel(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("nombre")
    val name: String? = null,
    @SerializedName("apellido")
    val lastName: String? = null,
    @SerializedName("acceso")
    val access: Boolean = false,
    @SerializedName("admin")
    val admin: Boolean? = null
)
