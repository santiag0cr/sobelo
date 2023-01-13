package com.ultimate.ultimatesophos.domain.managers

import android.content.res.Resources
import com.projects.sophosapp.R

class DocumentResourcesDataManager(private val resources: Resources) : DocumentResourceManager {
    override fun getDateAndDocType(date: String, docType: String): String = resources.getString(R.string.document_date_and_type, date, docType)
    override fun getUserName(name: String, lastname: String): String = resources.getString(R.string.document_username, name, lastname)
}
