package com.ultimate.ultimatesophos.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponseDto(
    var id: String? = null,
    var name: String? = null,
    var lastName: String? = null,
    var access: Boolean = false,
    var admin: Boolean? = null,
    var email: String? = null
) : Parcelable
