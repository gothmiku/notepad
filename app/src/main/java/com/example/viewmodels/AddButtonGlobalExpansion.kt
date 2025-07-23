package com.example.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel

class GlobalViewModel : ViewModel() {
    private val _globalBoolean = mutableStateOf(false)
    val globalBoolean: State<Boolean> = _globalBoolean

    fun toggleBoolean() {
        _globalBoolean.value = !_globalBoolean.value
    }

    fun setBoolean(value: Boolean) {
        _globalBoolean.value = value
    }

    fun getBoolean() : Boolean {
        return _globalBoolean.value
    }
}