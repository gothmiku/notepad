package com.example.database

import androidx.lifecycle.LiveData

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: LiveData<List<Note>> = noteDao.getAllLive()

    suspend fun addNote(note: Note) {
        noteDao.addNote(note)
    }

    suspend fun getAllNotes(): List<Note> {
        return noteDao.getAll()
    }

    suspend fun findById(id : Int) : Note{
        return noteDao.findById(id)
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    suspend fun deleteAll(){
        noteDao.deleteAll()
    }



}