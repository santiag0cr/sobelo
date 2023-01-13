package com.ultimate.ultimatesophos.core

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

@JvmOverloads
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

@JvmOverloads
fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

@JvmOverloads
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}