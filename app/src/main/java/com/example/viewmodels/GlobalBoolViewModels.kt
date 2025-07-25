package com.example.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.example.database.Note

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

class ShowNoteViewModel : ViewModel() {
    private val _globalBoolean = mutableStateOf(false)
    val globalBoolean: State<Boolean> = _globalBoolean

    fun toggleBoolean() {
        _globalBoolean.value = !_globalBoolean.value
    }

    fun setBoolean(value: Boolean) {
        _globalBoolean.value = value
    }

    fun getBoolean(): Boolean {
        return _globalBoolean.value
    }
}

class SelectedNoteViewModel : ViewModel() {
    private val _globalNote = mutableStateOf(Note(title="",content="",date=""))
    val globalBoolean: State<Note> = _globalNote

    fun setNote(value: Note) {
        _globalNote.value = value
    }

    fun getNote(): Note {
        return _globalNote.value
    }
}