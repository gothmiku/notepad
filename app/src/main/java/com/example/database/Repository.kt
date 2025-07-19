package com.example.database

import androidx.lifecycle.LiveData

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: LiveData<List<Note>> = noteDao.getAllLive()

    suspend fun addNote(note: Note) {
        noteDao.addNote(note)
    }

}