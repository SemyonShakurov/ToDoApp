package com.sscorp.todo.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    var id: Int?,
    val text: String,
    val priority: String,
    val done: Boolean,
    val deadline: Long?,
    val createdAt: Long,
    val updatedAt: Long,
    var syncState: String = "synced"
)