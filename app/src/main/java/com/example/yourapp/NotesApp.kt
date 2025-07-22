package com.example.yourapp

import android.app.Application

import com.example.database.NoteRepository
import com.example.database.AppDatabase

class NotesApp : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { NoteRepository(database.noteDao()) }
}
