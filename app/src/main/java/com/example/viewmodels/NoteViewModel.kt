package com.example.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.database.AppDatabase
import com.example.database.Note
import com.example.database.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.yourapp.NotesApp
import kotlinx.coroutines.withContext

class NoteViewModel(application: Application) : AndroidViewModel(application){
    val allNotes : LiveData<List<Note>>
    private val repository : NoteRepository

    init{
        val noteDao = AppDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        allNotes = repository.allNotes
    }

    fun addNote(note: Note?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (note != null) {
                Log.d("NoteViewModel", "Inserting note: $note")
                repository.addNote(note)
                Log.d("Note","Note has been added with ${note.uid}")
            }
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(note)
        }
    }

    val allNotesObserved : LiveData<List<Note>> = repository.allNotes

    suspend fun findById(id : Int) : Note?{
            return withContext(Dispatchers.IO){
                val note = repository.findById(id)
                Log.d("NoteViewModel", "Fetched note for id=$id: $note")
                note
        }

    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    suspend fun getAllNotes(): List<Note> {
        return withContext(Dispatchers.IO) {
            val value = repository.allNotes.value ?: emptyList()
            value
        }
    }

    fun delete(note : Note){
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(note)
        }
    }


    class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NoteViewModel(NotesApp()) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}