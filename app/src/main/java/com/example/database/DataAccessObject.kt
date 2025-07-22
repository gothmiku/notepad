package com.example.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.Date

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note ORDER BY uid DESC")
    fun getAll(): List<Note>

    @Query("SELECT * FROM Note ORDER BY uid DESC")
    fun getAllLive() : LiveData<List<Note>>

    @Query("SELECT title FROM Note")
    fun getAllTitles(): List<String>

    @Query("SELECT content FROM Note")
    fun getAllContents(): List<String>

    @Query("SELECT date FROM Note")
    fun getAllDates(): List<String>

    @Query("SELECT * FROM Note WHERE title LIKE :title")
    fun findByTitle(title: String): Note

    @Query("SELECT * FROM Note WHERE uid LIKE :uid LIMIT 1")
    fun findById(uid: Int): Note

    @Query("SELECT * FROM Note WHERE content LIKE :content")
    fun findByContent(content: String): Note

    @Query("SELECT * FROM Note WHERE date LIKE :date")
    fun findByDate(date: String): Note

    @Insert
    fun addNote(note: Note)

    @Insert
    fun insertAll(vararg notes: Note)

    @Delete
    fun delete(note: Note)

    @Query("DELETE FROM Note")
    fun deleteAll()

    @Update
    suspend fun updateNote(note: Note)

    //@Query("UPDATE sqlite_sequence SET seq = (SELECT MAX(uid) FROM Note) WHERE name='Note'")
    //suspend fun resetSequence()
}