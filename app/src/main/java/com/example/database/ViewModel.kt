package com.example.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ViewModel(application: Application) : AndroidViewModel(application){
    private val allNotes : LiveData<List<Note>>
    private val repository : NoteRepository


    init{
        val noteDao = AppDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        allNotes = repository.allNotes

    }

    fun addUser(note : Note){
        viewModelScope.launch(Dispatchers.IO){
            repository.addNote(note)
        }

    }
}