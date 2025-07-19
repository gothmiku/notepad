package com.example.database

import androidx.room.*
import java.util.Date

@Entity
data class Note(
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "content") var content: String?,
    @ColumnInfo(name = "date") val date: Date?
)

