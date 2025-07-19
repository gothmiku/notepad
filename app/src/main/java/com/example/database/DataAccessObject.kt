package com.example.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.util.Date

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note ORDER BY date DESC")
    fun getAll(): List<Note>

    @Query("SELECT * FROM Note ORDER BY date DESC")
    fun getAllLive() : LiveData<List<Note>>

    @Query("SELECT title FROM Note")
    fun getAllTitles(): List<String>

    @Query("SELECT content FROM Note")
    fun getAllContents(): List<String>

    @Query("SELECT date FROM Note")
    fun getAllDates(): List<String>

    @Query("SELECT * FROM Note WHERE title LIKE :title")
    fun findByTitle(title: String): Note

    @Query("SELECT * FROM Note WHERE content LIKE :content")
    fun findByContent(content: String): Note

    @Query("SELECT * FROM Note WHERE date LIKE :date")
    fun findByDate(date: String): Note

    @Query("SELECT * FROM Note WHERE uid IN (:noteIds)")
    fun loadAllByIds(noteIds: IntArray): List<Note>

    @Insert
    fun addNote(note: Note)

    @Insert
    fun insertAll(vararg notes: Note)

    @Delete
    fun delete(note: Note)

    @Delete
    fun deleteAll(notes: List<Note>)
}