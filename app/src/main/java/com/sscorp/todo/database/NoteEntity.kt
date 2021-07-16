package com.sscorp.todo.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "notes"
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long?,
    @ColumnInfo(name = "text")
    val text: String,
    @ColumnInfo(name = "priority")
    val priority: String,
    @ColumnInfo(name = "done")
    val done: Boolean,
    @ColumnInfo(name = "deadline")
    val deadline: Long?,
    @ColumnInfo(name = "syncState")
    var syncState: String = "synced"
)