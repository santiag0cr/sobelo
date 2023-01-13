package com.ultimate.ultimatesophos.domain.managers

interface DocumentResourceManager {
    fun getDateAndDocType(date: String, docType: String): String
    fun getUserName(name: String, lastname: String): String
}
